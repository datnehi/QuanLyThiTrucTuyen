package com.nhom6.server.Controller;

import com.nhom6.server.Model.Course;
import com.nhom6.server.Services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public List<Course> getAllMonHoc() {
        return courseService.getAllMonHoc();
    }

    @GetMapping("/{mamonhoc}")
    public Course getMonHocById(@PathVariable String mamonhoc) {
        return courseService.getMonHocById(mamonhoc);
    }

}
