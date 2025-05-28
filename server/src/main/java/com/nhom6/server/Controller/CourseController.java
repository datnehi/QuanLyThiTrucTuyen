package com.nhom6.server.Controller;

import com.nhom6.server.Model.Course;
import com.nhom6.server.Services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/all")
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

    @GetMapping
    public ResponseEntity<List<Course>> getAllSubjects() {
        List<Course> subjects = courseService.getAllSubjects();
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Course>> getActiveSubjects() {
        List<Course> subjects = courseService.getActiveSubjects();
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Course> createSubject(@RequestBody Course subject) {
        Course createdSubject = courseService.createSubject(subject);
        return new ResponseEntity<>(createdSubject, HttpStatus.CREATED);
    }

    @PutMapping("/{mamonhoc}")
    public ResponseEntity<Course> updateSubject(@PathVariable String mamonhoc, @RequestBody Course subject) {
        Course updatedSubject = courseService.updateSubject(mamonhoc, subject);
        return new ResponseEntity<>(updatedSubject, HttpStatus.OK);
    }

    @DeleteMapping("/{mamonhoc}")
    public ResponseEntity<Void> deleteSubject(@PathVariable String mamonhoc) {
        courseService.deleteSubject(mamonhoc);
        return new ResponseEntity<>(HttpStatus.OK); // Trả về 200 thay vì 204
    }

    @PatchMapping("/{mamonhoc}/toggle-status")
    public ResponseEntity<Void> toggleSubjectStatus(@PathVariable String mamonhoc) {
        courseService.toggleSubjectStatus(mamonhoc);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Course>> searchSubjects(@RequestParam String keyword) {
        List<Course> subjects = courseService.searchSubjects(keyword);
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

}
