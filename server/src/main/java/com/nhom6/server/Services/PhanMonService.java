package com.nhom6.server.Services;

import com.nhom6.server.Model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PhanMonService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Course> getMonHocBySinhVien(String id) {
        try {
            String sql = "SELECT * " +
                    "FROM phanmon pm " +
                    "JOIN monhoc mh ON pm.mamonhoc = mh.mamonhoc " +
                    "WHERE pm.id = ? AND pm.trangthai = 0";

            return jdbcTemplate.query(sql, new Object[]{id}, new BeanPropertyRowMapper<>(Course.class));
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Map<String, Object> addPhanMon(String id, String mamonhoc) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Kiểm tra người dùng có tồn tại không
            String checkUserSql = "SELECT COUNT(*) FROM nguoidung WHERE id = ?";
            int userCount = jdbcTemplate.queryForObject(checkUserSql, Integer.class, id);
            if (userCount == 0) {
                response.put("success", false);
                response.put("message", "Người dùng không tồn tại.");
                return response;
            }

            // Kiểm tra môn học có tồn tại không
            String checkSubjectSql = "SELECT COUNT(*) FROM monhoc WHERE mamonhoc = ?";
            int subjectCount = jdbcTemplate.queryForObject(checkSubjectSql, Integer.class, mamonhoc);
            if (subjectCount == 0) {
                response.put("success", false);
                response.put("message", "Môn học không tồn tại.");
                return response;
            }

            // Chèn vào bảng phanmon
            String sql = "INSERT INTO phanmon (id, mamonhoc, trangthai) VALUES (?, ?, ?)";
            int rowsAffected = jdbcTemplate.update(sql, id.trim(), mamonhoc.trim(), 0);

            // Trả về kết quả
            response.put("success", rowsAffected > 0);
            response.put("message", rowsAffected > 0 ? "Tham gia học phần thành công!" : "Tham gia học phần thất bại.");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Đã có lỗi xảy ra.");
            return response;
        }
    }
}
