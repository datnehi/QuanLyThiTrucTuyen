package com.nhom6.server.Controller;

import com.nhom6.server.DTO.AnswerDto;
import com.nhom6.server.Model.ChiTietBaiThi;
import com.nhom6.server.Services.ChiTietBaiThiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chitietbaithi")
public class ChiTietBaiThiController {
    @Autowired
    private ChiTietBaiThiService chiTietBaiThiService;

    @GetMapping("/{maketqua}")
    public ResponseEntity<List<ChiTietBaiThi>> getChiTietBaiLam(@PathVariable String maketqua) {
        List<ChiTietBaiThi> danhSachCauHoi = chiTietBaiThiService.getChiTietBaiLam(maketqua);
        return ResponseEntity.ok(danhSachCauHoi);
    }

//    @PostMapping("/{maketqua}")
//    public ResponseEntity<List<ChiTietBaiThi>> getChiTietBaiThi(@RequestBody List<Map<String, Object>> cauHoiList, @PathVariable String maketqua) {
//        List<ChiTietBaiThi> danhSachCauHoi = chiTietBaiThiService.getCauHoi(cauHoiList, maketqua);
//        return ResponseEntity.ok(danhSachCauHoi);
//    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveAnswer(@RequestBody AnswerDto answerDto){
        Map<String, String> response = new HashMap<>();
        try {
            chiTietBaiThiService.saveAnswer(answerDto);
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
