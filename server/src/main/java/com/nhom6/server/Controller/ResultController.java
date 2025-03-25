package com.nhom6.server.Controller;

import com.nhom6.server.DTO.ResultDto;
import com.nhom6.server.DTO.SubmitExamRequest;
import com.nhom6.server.Model.ChiTietBaiThi;
import com.nhom6.server.Model.Result;
import com.nhom6.server.Services.ChiTietBaiThiService;
import com.nhom6.server.Services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/results")
public class ResultController {
    @Autowired
    private ResultService resultService;
    private ChiTietBaiThiService chiTietBaiThiService;

    @GetMapping
    public ResponseEntity<List<Result>> getAllExams() {
        List<Result> results = resultService.getAllResults();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{maKiThi}")
    public ResponseEntity<List<Result>> getExamById(@PathVariable String maKiThi) {
            List<Result> result = resultService.getResultById(maKiThi);
            return ResponseEntity.ok(result);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<ResultDto>> getResultsWithUsers(@RequestParam String maKiThi, @RequestParam String maMonHoc) {
        List<ResultDto> result = resultService.getResultsWithUsers(maKiThi, maMonHoc);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{maKiThi}/{id}")
    public ResponseEntity<Result> getExamResult(@PathVariable String maKiThi, @PathVariable String id) {
        Result result = resultService.checkKetQua(maKiThi, id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create-exam")
    public ResponseEntity<?> startExam(@RequestParam String maKiThi, @RequestParam String id) {
        try {
            List<ChiTietBaiThi> listCauHoi = resultService.createExamResult(maKiThi, id);

            return ResponseEntity.ok(listCauHoi);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi tạo đề thi: " + e.getMessage());
        }
    }

    @PostMapping("/submit-exam")
    public ResponseEntity<Map<String, String>> submitExam(@RequestBody SubmitExamRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            resultService.submitExam(request);
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
