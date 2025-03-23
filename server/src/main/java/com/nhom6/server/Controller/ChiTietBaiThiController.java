package com.nhom6.server.Controller;

import com.nhom6.server.Model.ChiTietBaiThi;
import com.nhom6.server.Services.ChiTietBaiThiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<List<ChiTietBaiThi>> getChiTietBaiThi(@RequestBody List<Map<String, Object>> cauHoiList) {
        List<ChiTietBaiThi> danhSachCauHoi = chiTietBaiThiService.getCauHoi(cauHoiList);
        return ResponseEntity.ok(danhSachCauHoi);
    }
}
