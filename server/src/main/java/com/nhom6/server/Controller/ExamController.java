package com.nhom6.server.Controller;

import com.nhom6.server.Model.Exam;
import com.nhom6.server.Services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    // Lấy tất cả kỳ thi
    @GetMapping
    public ResponseEntity<List<Exam>> getAllExams() {
        List<Exam> exams = examService.getAllExams();
        return ResponseEntity.ok(exams);
    }

    // Lấy kỳ thi theo mã
    @GetMapping("/{maKiThi}")
    public ResponseEntity<Exam> getExamByMa(@PathVariable String maKiThi) {
        try {
            Exam exam = examService.getExamByMa(maKiThi);
            return ResponseEntity.ok(exam);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Tìm kiếm kỳ thi theo maMonHoc
    @GetMapping("/mamonhoc/{maMonHoc}")
    public ResponseEntity<List<Exam>> getExamByName(@PathVariable String maMonHoc) {
        List<Exam> exams = examService.getExamsByName(maMonHoc);
        return ResponseEntity.ok(exams);
    }

    // Tạo kỳ thi mới
    @PostMapping
    public ResponseEntity<Exam> createExam(@RequestBody Exam exam) {
        Exam createdExam = examService.createExam(exam);
        return ResponseEntity.ok(createdExam);
    }

    // Cập nhật kỳ thi
    @PutMapping("/{maKiThi}")
    public ResponseEntity<Exam> updateExam(@PathVariable String maKiThi, @RequestBody Exam examDetails) {
        try {
            Exam updatedExam = examService.updateExam(maKiThi, examDetails);
            return ResponseEntity.ok(updatedExam);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Xóa kỳ thi
    @DeleteMapping("/delete/{maKiThi}")
    public ResponseEntity<Void> deleteExam(@PathVariable String maKiThi) {
        examService.deleteExam(maKiThi);
        return ResponseEntity.noContent().build();
    }

    // Tìm kì thi theo id (sinh viên)
    @GetMapping("/id/{id}")
    public ResponseEntity<List<Exam>> getExamById(@PathVariable String id) {
        List<Exam> exams = examService.getExamsById(id);
        return ResponseEntity.ok(exams);
    }
}
