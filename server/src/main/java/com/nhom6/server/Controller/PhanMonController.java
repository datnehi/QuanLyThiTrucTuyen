package com.nhom6.server.Controller;

import com.nhom6.server.Model.Course;
import com.nhom6.server.Services.PhanMonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/phanmon")
public class PhanMonController {
    @Autowired
    private PhanMonService phanMonService;

    @GetMapping("/{id}")
    public List<Course> getMonHocBySinhVien(@PathVariable String id) {
        return phanMonService.getMonHocBySinhVien(id);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPhanMon(@RequestParam String id, @RequestParam String mamonhoc) {
        try {
            Map<String, Object> result = phanMonService.addPhanMon(id, mamonhoc);
            if (!(Boolean) result.get("success")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Lỗi máy chủ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
