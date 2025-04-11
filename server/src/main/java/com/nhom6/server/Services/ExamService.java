package com.nhom6.server.Services;

import com.nhom6.server.Model.Exam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ExamService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Lấy tất cả kỳ thi
    public List<Exam> getAllExams() {
        String sql = "SELECT * FROM kithi WHERE trangthai = 0 ORDER BY thoigiantao DESC";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Exam.class));
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Lấy kỳ thi theo maKiThi
    public Optional<Exam> getExamByMa(String maKiThi) {
        String sql = "SELECT * FROM kithi WHERE maKiThi = ? AND trangthai = 0";
        try {
            Exam exam = jdbcTemplate.queryForObject(sql, new Object[]{maKiThi}, new BeanPropertyRowMapper<>(Exam.class));
            return Optional.ofNullable(exam);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Lấy kỳ thi theo maMonHoc
    public List<Exam> getExamsByName(String maMonHoc) {
        String sql = "SELECT * FROM kithi WHERE maMonHoc = ? AND trangthai = 0 ORDER BY thoigiantao DESC";
        try {
            return jdbcTemplate.query(sql, new Object[]{maMonHoc}, new BeanPropertyRowMapper<>(Exam.class));
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Lấy kỳ thi theo id (sinh viên)
    public List<Exam> getExamsById(String id) {
        String sql = "SELECT * FROM kithi " +
                "JOIN phanmon ON kithi.mamonhoc = phanmon.mamonhoc " +
                "JOIN monhoc ON kithi.mamonhoc = monhoc.mamonhoc " +
                "WHERE phanmon.id = ? AND kithi.trangthai = 0 ORDER BY thoigiantao DESC";
        try {
            return jdbcTemplate.query(sql, new Object[]{id}, new BeanPropertyRowMapper<>(Exam.class));
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Tạo mã kỳ thi tiếp theo
    private String generateNextMaKiThi() {
        String sql = "SELECT makithi FROM kithi ORDER BY makithi DESC LIMIT 1";
        try {
            String lastMaKiThi = jdbcTemplate.queryForObject(sql, String.class);
            if (lastMaKiThi == null || lastMaKiThi.isEmpty()) {
                return "0000000001";
            }
            long number = Long.parseLong(lastMaKiThi);
            return String.format("%010d", ++number);
        } catch (Exception e) {
            return "0000000001";
        }
    }

    // Tạo kỳ thi mới
    public Optional<Exam> createExam(Exam exam) {
        try {
            String maKiThi = generateNextMaKiThi();
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

            return Optional.of(exam);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Cập nhật kỳ thi
    public Optional<Exam> updateExam(String maKiThi, Exam examDetails) {
        try {
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
                return Optional.empty();
            }

            return getExamByMa(maKiThi);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Xóa kỳ thi (ẩn)
    public boolean deleteExam(String maKiThi) {
        try {
            String sql = "UPDATE kithi SET trangThai = 1 WHERE maKiThi = ?";
            jdbcTemplate.update(sql, maKiThi);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
