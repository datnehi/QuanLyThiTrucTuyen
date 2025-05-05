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
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllMonHoc() {
        try {
            List<Course> courses = courseService.getAllMonHoc();
            return ResponseEntity.ok(Map.of(
                    "message", "Lấy danh sách môm học thành công",
                    "data", courses
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Lỗi khi lấy danh sách môm học"
            ));
        }
    }

    @GetMapping("/{mamonhoc}")
    public ResponseEntity<Map<String, Object>> getMonHocById(@PathVariable String mamonhoc) {
        try {
            Course course = courseService.getMonHocById(mamonhoc);
            return ResponseEntity.ok(Map.of(
                    "message", "Lấy môn học thành công",
                    "data", course
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy môn học với mã: " + mamonhoc
            ));
        }
    }

}
