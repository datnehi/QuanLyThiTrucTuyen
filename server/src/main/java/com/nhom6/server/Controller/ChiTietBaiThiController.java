package com.nhom6.server.Controller;

import com.nhom6.server.DTO.AnswerDto;
import com.nhom6.server.Model.ChiTietBaiThi;
import com.nhom6.server.Services.ChiTietBaiThiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/chitietbaithi")
public class ChiTietBaiThiController {

    @Autowired
    private ChiTietBaiThiService chiTietBaiThiService;

    // -------------------- GET: Lấy chi tiết bài làm theo mã kết quả --------------------
    @GetMapping("/{maketqua}")
    public ResponseEntity<Map<String, Object>> getChiTietBaiLam(@PathVariable String maketqua) {
        try {
            List<ChiTietBaiThi> danhSachCauHoi = chiTietBaiThiService.getChiTietBaiLam(maketqua);
            return ResponseEntity.ok(Map.of(
                    "message", "Lấy danh sách câu hỏi thành công",
                    "data", danhSachCauHoi
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Lỗi khi lấy chi tiết bài làm",
                    "error", e.getMessage()
            ));
        }
    }

    // -------------------- POST: Lưu đáp án --------------------
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveAnswer(@RequestBody AnswerDto answerDto) {
        try {
            chiTietBaiThiService.saveAnswer(answerDto);
            return ResponseEntity.ok(Map.of(
                    "message", "Lưu đáp án thành công"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Lỗi khi lưu đáp án",
                    "error", e.getMessage()
            ));
        }
    }
}
