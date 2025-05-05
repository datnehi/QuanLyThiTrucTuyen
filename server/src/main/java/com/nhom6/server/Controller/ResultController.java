package com.nhom6.server.Controller;

import com.nhom6.server.DTO.ChiTietBaiThiDto;
import com.nhom6.server.DTO.ResultDto;
import com.nhom6.server.DTO.SubmitExamRequest;
import com.nhom6.server.Model.Exam;
import com.nhom6.server.Model.Result;
import com.nhom6.server.Services.ExamService;
import com.nhom6.server.Services.ResultService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    @Autowired
    private ResultService resultService;

    @Autowired
    private ExamService examService;

    // -------------------- GET: Lấy tất cả kết quả --------------------
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllResults() {
        try {
            List<Result> results = resultService.getAllResults();
            return ResponseEntity.ok(Map.of(
                    "message", "Lấy tất cả kết quả thành công",
                    "data", results
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Lỗi khi lấy tất cả kết quả"
            ));
        }
    }

    // -------------------- GET: Lấy kết quả theo mã kỳ thi --------------------
    @GetMapping("/{maKiThi}")
    public ResponseEntity<Map<String, Object>> getResultsByExam(@PathVariable String maKiThi) {
        try {
            List<Result> results = resultService.getResultById(maKiThi);
            return ResponseEntity.ok(Map.of(
                    "message", "Lấy kết quả theo kỳ thi thành công",
                    "data", results
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Lỗi khi lấy kết quả theo kỳ thi"
            ));
        }
    }

    // -------------------- GET: Lấy kết quả chi tiết theo kỳ thi và userId --------------------
    @GetMapping("/{maKiThi}/{id}")
    public ResponseEntity<Map<String, Object>> getUserExamResult(@PathVariable String maKiThi, @PathVariable String id) {
        try {
            Result result = resultService.checkKetQua(maKiThi, id);
            if (result == null) {
                return ResponseEntity.ok(Map.of(
                        "message", "Sinh viên chưa thi"
                ));
            }
            return ResponseEntity.ok(Map.of(
                    "message", "Lấy kết quả chi tiết thành công",
                    "data", result
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Lỗi khi lấy kết quả chi tiết"
            ));
        }
    }

    // -------------------- GET: Lấy kết quả + người dùng theo mã kỳ thi và môn học --------------------
    @GetMapping("/getall")
    public ResponseEntity<Map<String, Object>> getResultsWithUsers(
            @RequestParam String maKiThi,
            @RequestParam String maMonHoc) {
        try {
            List<ResultDto> result = resultService.getResultsWithUsers(maKiThi, maMonHoc);
            return ResponseEntity.ok(Map.of(
                    "message", "Lấy kết quả cùng thông tin người dùng thành công",
                    "data", result
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Lỗi khi lấy kết quả kèm người dùng"
            ));
        }
    }

    // -------------------- POST: Bắt đầu làm bài thi --------------------
    @PostMapping("/create-exam")
    public ResponseEntity<Map<String, Object>> startExam(
            @RequestParam String maKiThi,
            @RequestParam String id) {
        try {
            Optional<Exam> kiThi = examService.getExamByMa(maKiThi);
            if (kiThi.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of(
                        "message", "Không tìm thấy kỳ thi"
                ));
            }

            List<ChiTietBaiThiDto> listCauHoi = resultService.createExamResult(maKiThi, id);
            resultService.startExam(maKiThi, id, kiThi.get().getThoiGianThi());

            return ResponseEntity.ok(Map.of(
                    "message", "Bắt đầu làm bài thành công",
                    "data", listCauHoi
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Lỗi khi tạo đề thi",
                    "error", e.getMessage()
            ));
        }
    }

    // -------------------- POST: Nộp bài thi --------------------
    @PostMapping("/submit-exam")
    public ResponseEntity<Map<String, Object>> submitExam(@RequestBody SubmitExamRequest request) {
        try {
            resultService.submitExam(request);
            return ResponseEntity.ok(Map.of(
                    "message", "Nộp bài thành công"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Lỗi khi nộp bài",
                    "error", e.getMessage()
            ));
        }
    }
}
