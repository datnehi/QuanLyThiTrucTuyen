package com.nhom6.server.Services;

import com.nhom6.server.Model.Exam;
import com.nhom6.server.Model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
}
