package com.nhom6.server.Services;


import com.nhom6.server.Model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Course> getAllMonHoc() {
        String sql = "SELECT * FROM monhoc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Course.class));
    }

    public Course getMonHocById(String maMonHoc) {
        String sql = "SELECT * FROM monhoc WHERE maMonHoc = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{maMonHoc}, new BeanPropertyRowMapper<>(Course.class));
    }
}
