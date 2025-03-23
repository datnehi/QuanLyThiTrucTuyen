package com.nhom6.server.Services;

import com.nhom6.server.DTO.SubmitExamRequest;
import com.nhom6.server.Model.ChiTietBaiThi;
import com.nhom6.server.Model.Exam;
import com.nhom6.server.Model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ResultService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ChiTietBaiThiService chiTietBaiThiService;
    @Autowired
    private  ExamService examService;

    public List<Result> getAllResults() {
        String sql = "SELECT * FROM ketqua";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Result.class));
    }

    public List<Result> getResultById(String maKiThi) {
        String sql = "SELECT * FROM ketqua WHERE maKiThi = ?";
        return jdbcTemplate.query(sql, new Object[]{maKiThi}, new BeanPropertyRowMapper<>(Result.class));
    }

//    public Exam getExamById(String maKiThi) {
//        String sql = "SELECT * FROM kithi WHERE maKiThi = ?";
//        return jdbcTemplate.queryForObject(sql, new Object[]{maKiThi}, new BeanPropertyRowMapper<>(Exam.class));
//    }

    private String generateNextMaKetQua() {
        String sql = "SELECT TOP 1 maketqua FROM ketqua ORDER BY maketqua DESC";
        String lastMaKetQua = jdbcTemplate.queryForObject(sql, String.class);
        if (lastMaKetQua == null || lastMaKetQua.isEmpty()) {
            return "0000000001"; // Nếu chưa có mã nào, bắt đầu từ 0000000001
        }
        // Chuyển mã cuối thành số rồi tăng lên
        long number = Long.parseLong(lastMaKetQua);
        number++;
        // Format lại thành chuỗi số 10 ký tự
        return String.format("%010d", number);
    }

    // Lưu kết quả bài thi mới
    public String createResult(String maKiThi, String id) {
        String maKetQua = generateNextMaKetQua(); // Tạo mã kết quả
        String sql = """
            INSERT INTO ketqua (maketqua, makithi, id, diem, thoigianvaothi, thoigianlambai, socaudung)
            VALUES (?, ?, ?, null, GETDATE(), null, null)
        """;
        jdbcTemplate.update(sql, maKetQua, maKiThi, id);
        return maKetQua;
    }

    // Kiểm tra xem sinh viên đã có kết quả chưa
    public Result checkKetQua(String maKiThi, String id) {
        String sql = "SELECT * FROM ketqua WHERE maKiThi = ? AND id = ?";
        try {
            List<Result> results = jdbcTemplate.query(sql, new Object[]{maKiThi, id}, new BeanPropertyRowMapper<>(Result.class));
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            return null;
        }
    }

    public List<ChiTietBaiThi> createExamResult(String maKiThi, String id) {
        // 🔍 Kiểm tra kỳ thi có tồn tại không
        Exam kiThi = examService.getExamByMa(maKiThi);

        // 🔍 Kiểm tra xem sinh viên đã có kết quả thi chưa
        Result existingKetQua = this.checkKetQua(kiThi.getMaKiThi(), id);

        // Nếu chưa có, tạo mới
        String maKetQua = existingKetQua == null ? this.createResult(maKiThi, id) : existingKetQua.getMaKetQua();
        List<String> existingCauHoiIds = chiTietBaiThiService.checkCauHoi(maKetQua);
        List<Map<String, Object>> cauHoiList;

        if (!existingCauHoiIds.isEmpty()) {
            // Nếu đã có, lấy lại từ bảng `cauhoi`
            String placeholders = String.join(",", Collections.nCopies(existingCauHoiIds.size(), "?"));
            String selectCauHoiSql = "SELECT * FROM cauhoi WHERE maCauHoi IN (" + placeholders + ")";
            cauHoiList = jdbcTemplate.queryForList(selectCauHoiSql, existingCauHoiIds.toArray());
        } else {
            // Nếu chưa có, lấy ngẫu nhiên theo môn học
            cauHoiList = chiTietBaiThiService.randomCauHoi(maKetQua, kiThi.getMaMonHoc(), kiThi.getSoCau());
        }
        return chiTietBaiThiService.getCauHoi(cauHoiList);
    }

    public void submitExam(SubmitExamRequest request) {
        // Kiểm tra hoặc tạo kết quả mới
        Result result = checkKetQua(request.getMaKiThi(), request.getId());
        if (result == null) {
            throw new IllegalArgumentException("Không tìm thấy kết quả thi.");
        }

        Exam exam = examService.getExamByMa(request.getMaKiThi());
        int correctCount = 0;

        for (Map.Entry<String, String> entry : request.getAnswers().entrySet()) {
            String maCauHoi = entry.getKey();
            String dapAnChon = entry.getValue();

            // Nếu dapAnChon null, bỏ qua (tức là không chọn câu nào)
            if (dapAnChon == null || dapAnChon.isEmpty()) continue;

            // Kiểm tra xem đã có dòng nào trong chitietde chưa, nếu chưa thì INSERT trước
            String insertQuery = "MERGE INTO chitietde AS target " +
                    "USING (SELECT ? AS maketqua, ? AS macauhoi, ? AS dapanchon) AS source " +
                    "ON target.maketqua = source.maketqua AND target.macauhoi = source.macauhoi " +
                    "WHEN MATCHED THEN UPDATE SET target.dapanchon = source.dapanchon " +
                    "WHEN NOT MATCHED THEN INSERT (maketqua, macauhoi, dapanchon) VALUES (source.maketqua, source.macauhoi, source.dapanchon);";
            jdbcTemplate.update(insertQuery, result.getMaKetQua(), maCauHoi, dapAnChon);

            // Kiểm tra đáp án đúng
            String checkQuery = "SELECT COUNT(*) FROM cautraloi WHERE macautraloi = ? AND ladapan = 1";
            int isCorrect = jdbcTemplate.queryForObject(checkQuery, Integer.class, dapAnChon);
            correctCount += isCorrect;
        }

        // Tính điểm
        float diem = (10.0f / exam.getSoCau()) * correctCount;

        // Cập nhật ketqua
        String updateKetQua = "UPDATE ketqua SET diem = ?, socaudung = ?, thoigianlambai = ? WHERE maketqua = ?";
        jdbcTemplate.update(updateKetQua, diem, correctCount, request.getTimeUsed(), result.getMaKetQua());
    }
}

