package com.nhom6.server.Services;

import com.nhom6.server.Model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Optional<Map<String, Object>> login(String id, String matkhau) {
        String sql = "SELECT * FROM nguoidung WHERE id = ? AND matkhau = ? AND trangthai = 0";
        try {
            return Optional.of(jdbcTemplate.queryForMap(sql, id, matkhau));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Account> getAll() {
        String sql = "SELECT * FROM nguoidung";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Account.class));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

}