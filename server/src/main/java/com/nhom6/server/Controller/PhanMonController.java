package com.nhom6.server.Controller;

import com.nhom6.server.Model.Course;
import com.nhom6.server.Services.PhanMonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/phanmon")
public class PhanMonController {

    @Autowired
    private PhanMonService phanMonService;

    // -------------------- GET: Lấy danh sách môn học theo ID sinh viên --------------------
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getMonHocBySinhVien(@PathVariable String id) {
        try {
            List<Course> courses = phanMonService.getMonHocBySinhVien(id);
            return ResponseEntity.ok(Map.of(
                    "message", "Lấy danh sách môn học theo sinh viên thành công",
                    "data", courses
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Lỗi khi lấy danh sách môn học",
                    "error", e.getMessage()
            ));
        }
    }

    // -------------------- POST: Phân môn học cho sinh viên --------------------
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addPhanMon(
            @RequestParam String id,
            @RequestParam String mamonhoc) {
        try {
            Map<String, Object> result = phanMonService.addPhanMon(id, mamonhoc);
            boolean success = (boolean) result.getOrDefault("success", false);

            if (!success) {
                return ResponseEntity.badRequest().body(result);
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Phân môn học thành công",
                    "data", result.get("data") // nếu bạn trả về gì đó trong "data"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Lỗi khi phân môn học",
                    "error", e.getMessage()
            ));
        }
    }
}
