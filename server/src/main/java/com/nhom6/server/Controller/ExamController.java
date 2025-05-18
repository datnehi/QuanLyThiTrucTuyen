package com.nhom6.server.Controller;

import com.nhom6.server.Model.Exam;
import com.nhom6.server.Services.ExamService;
import com.nhom6.server.Services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    @Autowired
    private QuestionService questionService;

    // -------------------- GET: Lấy tất cả kỳ thi --------------------
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllExams() {
        try {
            List<Exam> exams = examService.getAllExams();
            return ResponseEntity.ok(Map.of(
                    "message", "Lấy danh sách kỳ thi thành công",
                    "data", exams
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Lỗi khi lấy danh sách kỳ thi"
            ));
        }
    }

    // -------------------- GET: Lấy kỳ thi theo mã --------------------
    @GetMapping("/{maKiThi}")
    public ResponseEntity<Map<String, Object>> getExamByMa(@PathVariable String maKiThi) {
        try {
            Optional<Exam> exam = examService.getExamByMa(maKiThi);
            return ResponseEntity.ok(Map.of(
                    "message", "Lấy kỳ thi thành công",
                    "data", exam
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy kỳ thi với mã: " + maKiThi
            ));
        }
    }

    // -------------------- GET: Tìm theo mã môn học --------------------
    @GetMapping("/mamonhoc/{maMonHoc}")
    public ResponseEntity<Map<String, Object>> getExamByMaMonHoc(@PathVariable String maMonHoc) {
        try {
            List<Exam> exams = examService.getExamsByMaMonHoc(maMonHoc);
            return ResponseEntity.ok(Map.of(
                    "message", "Tìm kiếm kỳ thi theo môn học thành công",
                    "data", exams
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Lỗi khi tìm kiếm kỳ thi theo môn học"
            ));
        }
    }

    // -------------------- GET: Tìm theo ID sinh viên --------------------
    @GetMapping("/sinhvien/{id}")
    public ResponseEntity<Map<String, Object>> getExamById(@PathVariable String id) {
        try {
            List<Exam> exams = examService.getExamsById(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Lấy danh sách kỳ thi của sinh viên thành công",
                    "data", exams
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Lỗi khi lấy kỳ thi theo sinh viên"
            ));
        }
    }

    // -------------------- POST: Tạo kỳ thi mới --------------------
    @PostMapping
    public ResponseEntity<Map<String, Object>> createExam(@RequestBody Exam exam) {
        // Kiểm tra dữ liệu hợp lệ
        if (exam.getTenKiThi() == null || exam.getTenKiThi().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Tên kỳ thi không được để trống"));
        }

        if (exam.getMaMonHoc() == null || exam.getMaMonHoc().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mã môn học không được để trống"));
        }

        if (exam.getThoiGianBatDau() == null || exam.getThoiGianKetThuc() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Thời gian bắt đầu và kết thúc không được để trống"));
        }

        if (exam.getThoiGianBatDau().isAfter(exam.getThoiGianKetThuc()) || exam.getThoiGianBatDau().isEqual(exam.getThoiGianKetThuc())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc"));
        }

        if (exam.getThoiGianThi() <= 0) {
            return ResponseEntity.badRequest().body(Map.of("message", "Thời gian thi phải lớn hơn 0"));
        }

        if (exam.getSoCau() <= 0) {
            return ResponseEntity.badRequest().body(Map.of("message", "Số câu phải lớn hơn 0"));
        }

        int soCauHoiHienCo = questionService.countQuestionsByMaMonHoc(exam.getMaMonHoc());
        if (soCauHoiHienCo < exam.getSoCau()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Không đủ câu hỏi để tạo kỳ thi. Hiện có " + soCauHoiHienCo + " câu hỏi."));
        }

        try {
            Optional<Exam> createdExam = examService.createExam(exam);
            return ResponseEntity.status(201).body(Map.of(
                    "message", "Tạo kỳ thi thành công",
                    "data", createdExam
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                    "message", "Lỗi khi tạo kỳ thi"
            ));
        }
    }

    // -------------------- PUT: Cập nhật kỳ thi --------------------
    @PutMapping("/{maKiThi}")
    public ResponseEntity<Map<String, Object>> updateExam(@PathVariable String maKiThi, @RequestBody Exam examDetails) {
        // Kiểm tra dữ liệu hợp lệ
        if (examDetails.getTenKiThi() == null || examDetails.getTenKiThi().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Tên kỳ thi không được để trống"));
        }

        if (examDetails.getThoiGianKetThuc() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Thời gian kết thúc không được để trống"));
        }

        if (examDetails.getThoiGianBatDau().isAfter(examDetails.getThoiGianKetThuc()) || examDetails.getThoiGianBatDau().isEqual(examDetails.getThoiGianKetThuc())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Thời gian kết thúc phải lớn hơn thời gian bắt đầu"));
        }

        try {
            Optional<Exam> updatedExam = examService.updateExam(maKiThi, examDetails);
            return ResponseEntity.ok(Map.of(
                    "message", "Cập nhật kỳ thi thành công",
                    "data", updatedExam
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy kỳ thi để cập nhật"
            ));
        }
    }

    // -------------------- DELETE: Xóa kỳ thi --------------------
    @DeleteMapping("/{maKiThi}")
    public ResponseEntity<Map<String, Object>> deleteExam(@PathVariable String maKiThi) {
        try {
            examService.deleteExam(maKiThi);
            return ResponseEntity.ok(Map.of(
                    "message", "Xóa kỳ thi thành công"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy kỳ thi để xóa"
            ));
        }
    }
}
