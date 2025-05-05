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

    public List<Course> getAllMonHoc() {
        return courseRepository.findByTrangthaiFalse();
    }

    public Optional<Course> getMonHocById(String maMonHoc) {
        return courseRepository.findById(maMonHoc)
                .filter(course -> !course.isTrangthai());
    }
}
