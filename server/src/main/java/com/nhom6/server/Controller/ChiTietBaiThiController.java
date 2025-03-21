package com.nhom6.server.Controller;

import com.nhom6.server.Model.ChiTietBaiThi;
import com.nhom6.server.Services.ChiTietBaiThiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
