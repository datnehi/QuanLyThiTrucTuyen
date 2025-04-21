package com.nhom6.server.Controller;

import com.nhom6.server.Model.NguoiDung;
import com.nhom6.server.Services.NguoiDungServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nguoidung")
public class NguoiDungController {

    @Autowired
    private NguoiDungServiceImpl nguoiDungService;

    // Lấy tất cả người dùng
    @GetMapping
    public ResponseEntity<List<NguoiDung>> getAllUsers() {
        List<NguoiDung> users = nguoiDungService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Lấy danh sách người dùng đang hoạt động (trạng thái = false)
    @GetMapping("/active")
    public ResponseEntity<List<NguoiDung>> getActiveUsers() {
        List<NguoiDung> activeUsers = nguoiDungService.getActiveUsers();
        return new ResponseEntity<>(activeUsers, HttpStatus.OK);
    }

    // Tạo mới người dùng
    @PostMapping
    public ResponseEntity<NguoiDung> createUser(@RequestBody NguoiDung nguoiDung) {
        NguoiDung createdUser = nguoiDungService.createUser(nguoiDung);
        return ResponseEntity.ok(createdUser);
    }

    // Cập nhật thông tin người dùng
    @PutMapping("/{id}")
    public ResponseEntity<NguoiDung> updateUser(
            @PathVariable String id,
            @RequestBody NguoiDung nguoiDung) {
        NguoiDung updatedUser = nguoiDungService.updateUser(id, nguoiDung);
        return ResponseEntity.ok(updatedUser);
    }

    // Xóa người dùng (cập nhật trạng thái = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        nguoiDungService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Chuyển đổi trạng thái người dùng (active/inactive)
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<NguoiDung> toggleUserStatus(@PathVariable String id) {
        nguoiDungService.toggleUserStatus(id);
        return ResponseEntity.ok().build();
    }

    // Tìm kiếm người dùng theo từ khóa (họ tên hoặc email)
    @GetMapping("/search")
    public ResponseEntity<List<NguoiDung>> searchUsers(@RequestParam String keyword) {
        List<NguoiDung> result = nguoiDungService.searchUsers(keyword);
        return ResponseEntity.ok(result);
    }
}