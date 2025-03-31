package com.nhom6.server.Services;

import com.nhom6.server.DTO.ResultDto;
import com.nhom6.server.DTO.SubmitExamRequest;
import com.nhom6.server.Model.ChiTietBaiThi;
import com.nhom6.server.Model.Exam;
import com.nhom6.server.Model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

@Service
public class ResultService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ChiTietBaiThiService chiTietBaiThiService;
    @Autowired
    private  ExamService examService;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final ConcurrentHashMap<String, ScheduledFuture<?>> runningTimers = new ConcurrentHashMap<>();
    private String generateKey(String maKiThi, String id) {
        return maKiThi + "_" + id;
    }

    // Bắt đầu bài thi, tạo bộ đếm ngược
    public void startExam(String maKiThi, String id, int durationMinutes) {
        String key = generateKey(maKiThi, id);
        // Kiểm tra nếu đã có bộ đếm cũ thì không tạo mới
        if (runningTimers.containsKey(key)) {
            return;
        }
        Runnable autoSubmitTask = () -> autoSubmitExam(maKiThi, id, durationMinutes);
        ScheduledFuture<?> scheduledTask = scheduler.schedule(autoSubmitTask, durationMinutes + 1 , TimeUnit.MINUTES);

        if (scheduledTask != null) {
            runningTimers.put(key, scheduledTask);
        }
    }

    // Nếu hết thời gian mà chưa nộp thì tự động nộp bài
    private void autoSubmitExam(String maKiThi, String id, int durationMinutes) {
        String key = generateKey(maKiThi, id);
        if (runningTimers.containsKey(key)) { // Kiểm tra nếu còn đếm ngược
            runningTimers.remove(key); // Xóa bộ đếm ngược
            submitExam(new SubmitExamRequest(maKiThi, id, durationMinutes)); // Gọi hàm nộp bài
        }
    }

    public List<Result> getAllResults() {
        String sql = "SELECT * FROM ketqua";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Result.class));
    }

    public List<Result> getResultById(String maKiThi) {
        String sql = "SELECT * FROM ketqua WHERE maKiThi = ?";
        return jdbcTemplate.query(sql, new Object[]{maKiThi}, new BeanPropertyRowMapper<>(Result.class));
    }

    public List<ResultDto> getResultsWithUsers(String maKiThi, String maMonHoc) {
        String sql = "SELECT * FROM phanmon pm " +
                "JOIN nguoidung nd ON pm.id = nd.id " +
                "LEFT JOIN ketqua kq ON pm.id = kq.id AND kq.maKiThi = ? " +
                "WHERE pm.maMonHoc = ?";

        return jdbcTemplate.query(sql, new Object[]{maKiThi, maMonHoc}, (rs, rowNum) -> {
            ResultDto result = new ResultDto();
            result.setMaKetQua(rs.getString("maketqua"));
            result.setId(rs.getString("id"));
            result.setHoten(rs.getString("hoten"));
            result.setDiem(rs.getObject("diem") != null ? rs.getFloat("diem") : null);
            result.setThoiGianVaoThi(rs.getTimestamp("thoigianvaothi") != null
                    ? rs.getTimestamp("thoigianvaothi").toLocalDateTime()
                    : null);
            result.setThoiGianLamBai(rs.getObject("thoigianlambai") != null ? rs.getFloat("thoigianlambai") : null);
            return result;
        });
    }

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
            return jdbcTemplate.queryForObject(sql, new Object[]{maKiThi, id}, new BeanPropertyRowMapper<>(Result.class));
        } catch (EmptyResultDataAccessException e) {
            // Không có kết quả, trả về null
            return null;
        }
    }


    public List<ChiTietBaiThi> createExamResult(String maKiThi, String id) {
        // 🔍 Kiểm tra kỳ thi có tồn tại không
        Exam kiThi = examService.getExamByMa(maKiThi);

        // 🔍 Kiểm tra xem sinh viên đã có kết quả thi chưa
        Result existingKetQua = this.checkKetQua(kiThi.getMaKiThi(), id);
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
        return chiTietBaiThiService.getCauHoi(cauHoiList, maKetQua);
    }

    public void submitExam(SubmitExamRequest request) {
        if (runningTimers.containsKey(request.getId())) {
            runningTimers.get(request.getId()).cancel(false); // Hủy bộ đếm ngược
            runningTimers.remove(request.getId()); // Xóa khỏi danh sách
        }
        // Kiểm tra
        Result result = checkKetQua(request.getMaKiThi(), request.getId());
        if (result == null) {
            throw new IllegalArgumentException("Không tìm thấy kết quả thi.");
        }

        Exam exam = examService.getExamByMa(request.getMaKiThi());

        // Lấy danh sách đáp án đã chọn từ bảng chitietde
        String selectQuery = "SELECT macauhoi, dapanchon FROM chitietde WHERE maketqua = ?";
        List<Map<String, Object>> answers = jdbcTemplate.queryForList(selectQuery, result.getMaKetQua());

        int correctCount = 0;

        for (Map<String, Object> answer : answers) {
            String macauhoi = (String) answer.get("macauhoi");
            String dapAnChon = (String) answer.get("dapanchon");

            if (dapAnChon == null || dapAnChon.isEmpty()) continue;

            // Kiểm tra đáp án đúng
            String checkQuery = "SELECT COUNT(*) FROM cautraloi WHERE macautraloi = ? AND ladapan = 1";
            int isCorrect = jdbcTemplate.queryForObject(checkQuery, Integer.class, dapAnChon);
            correctCount += isCorrect;
        }

        // Tính điểm
        float diem = (10.0f / exam.getSoCau()) * correctCount;

        // Cập nhật bảng ketqua
        String updateKetQua = "UPDATE ketqua SET diem = ?, socaudung = ?, thoigianlambai = ? WHERE maketqua = ?";
        jdbcTemplate.update(updateKetQua, diem, correctCount, request.getTimeUsed(), result.getMaKetQua());
    }

}

