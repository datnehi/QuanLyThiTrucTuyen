package com.nhom6.server.Services;


import com.nhom6.server.Model.Course;
import com.nhom6.server.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    // Lấy tất cả các môn học có trạng thái là false (trangthai = 0)
    public List<Course> getAllMonHoc() {
        return courseRepository.findByTrangThaiFalse();
    }

    // Lấy môn học theo mã môn học và trạng thái false
    public Course getMonHocById(String maMonHoc) {
        Optional<Course> course = courseRepository.findByMaMonHocAndTrangThaiFalse(maMonHoc);
        return course.orElseThrow(() -> new RuntimeException("Môn học không tồn tại!"));
    }
}
