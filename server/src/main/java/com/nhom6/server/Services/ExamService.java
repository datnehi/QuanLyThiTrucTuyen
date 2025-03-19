package com.nhom6.server.Services;

import com.nhom6.server.Model.Exam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Lấy tất cả kỳ thi
    public List<Exam> getAllExams() {
        String sql = "SELECT * FROM kithi WHERE trangthai = 0";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Exam.class));
    }

    // Lấy kỳ thi theo maMonHoc
    public List<Exam> getExamsByName(String maMonHoc) {
        String sql = "SELECT * FROM kithi WHERE maMonHoc = ?";
        return jdbcTemplate.query(sql, new Object[]{maMonHoc}, new BeanPropertyRowMapper<>(Exam.class));
    }

    // Tạo mã kỳ thi tiếp theo
    private String generateNextMaKiThi() {
        String sql = "SELECT TOP 1 makithi FROM kithi ORDER BY makithi DESC";
        String lastMaKiThi = jdbcTemplate.queryForObject(sql, String.class);

        if (lastMaKiThi == null || lastMaKiThi.isEmpty()) {
            return "0000000001"; // Nếu chưa có mã nào, bắt đầu từ 0000000001
        }

        // Chuyển mã cuối thành số rồi tăng lên
        long number = Long.parseLong(lastMaKiThi);
        number++;

        // Format lại thành chuỗi số 10 ký tự
        return String.format("%010d", number);
    }

    // Tạo kỳ thi mới
    public Exam createExam(Exam exam) {
        String maKiThi = generateNextMaKiThi(); // Sinh mã mới
        exam.setMaKiThi(maKiThi);

        String sql = "INSERT INTO kithi (maKiThi, maMonHoc, tenKiThi, thoiGianTao, thoiGianThi, thoiGianBatDau, thoiGianKetThuc, xemDiem, xemDapAn, hienThiBaiLam, soCau, trangThai) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
                exam.getMaKiThi(),
                exam.getMaMonHoc(),
                exam.getTenKiThi(),
                exam.getThoiGianTao(),
                exam.getThoiGianThi(),
                exam.getThoiGianBatDau(),
                exam.getThoiGianKetThuc(),
                exam.getXemDiem(),
                exam.getXemDapAn(),
                exam.getHienThiBaiLam(),
                exam.getSoCau(),
                exam.getTrangThai()
        );

        return exam;
    }

    // Cập nhật kỳ thi
    public Exam updateExam(String maKiThi, Exam examDetails) {
        String sql = "UPDATE kithi SET tenKiThi = ?, thoiGianTao = ?, thoiGianThi = ?, thoiGianBatDau = ?, thoiGianKetThuc = ?, " +
                "xemDiem = ?, xemDapAn = ?, hienThiBaiLam = ?, soCau = ?, trangThai = ? WHERE maKiThi = ?";

        int rowsAffected = jdbcTemplate.update(sql,
                examDetails.getTenKiThi(),
                examDetails.getThoiGianTao(),
                examDetails.getThoiGianThi(),
                examDetails.getThoiGianBatDau(),
                examDetails.getThoiGianKetThuc(),
                examDetails.getXemDiem(),
                examDetails.getXemDapAn(),
                examDetails.getHienThiBaiLam(),
                examDetails.getSoCau(),
                examDetails.getTrangThai(),
                maKiThi
        );

        if (rowsAffected == 0) {
            throw new RuntimeException("Không tìm thấy kỳ thi!");
        }

        return getExamById(maKiThi);
    }

    // Xóa kỳ thi
    public void deleteExam(String maKiThi) {
        String sql = "UPDATE kithi SET trangThai = 1 WHERE maKiThi = ?";
        jdbcTemplate.update(sql, maKiThi);
    }

    // Lấy kỳ thi theo mã
    public Exam getExamById(String maKiThi) {
        String sql = "SELECT * FROM kithi WHERE maKiThi = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{maKiThi}, new BeanPropertyRowMapper<>(Exam.class));
    }
}
