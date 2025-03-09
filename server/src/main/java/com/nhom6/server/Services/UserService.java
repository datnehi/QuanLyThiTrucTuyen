package com.nhom6.server.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> login(String id, String matkhau) {
        String sql = "SELECT * FROM nguoidung WHERE id = ? AND matkhau = ? AND trangthai = 0";

        try {
            return jdbcTemplate.queryForMap(sql, id, matkhau);
        } catch (Exception e) {
            return null;
        }
    }
}