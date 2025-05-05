package com.nhom6.server.Controller;

import com.nhom6.server.Model.User;
import com.nhom6.server.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest, HttpSession session) {
        try {
            String id = loginRequest.get("id");
            String matkhau = loginRequest.get("matkhau");

            if (id == null || matkhau == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Thiếu thông tin đăng nhập!"));
            }

            if (!id.matches("^\\d{7}$")) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("message", "Mã sinh viên phải có 7 số!"));
            }

            Optional<User> user = userService.login(id, matkhau);

            if (user.isPresent()) {
                String token = UUID.randomUUID().toString();
                session.setAttribute("user", user);
                session.setAttribute("userToken", token);

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Đăng nhập thành công");
                response.put("userToken", token);
                response.put("user", user);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Sai ID, mật khẩu hoặc tài khoản đã bị khóa"));
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Đã xảy ra lỗi trong quá trình đăng nhập"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        try {
            session.invalidate();
            return ResponseEntity.ok(Map.of("message", "Đăng xuất thành công"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Đã xảy ra lỗi khi đăng xuất"));
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<Map<String, Object>> getAllAccounts() {
        try {
            List<User> users = userService.getAll();
            if (users.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body(Map.of("message", "Không có tài khoản nào"));
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Lấy danh sách tài khoản thành công",
                    "data", users
            ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Đã xảy ra lỗi khi lấy danh sách tài khoản"));
        }
    }

//    // Lấy tất cả người dùng
//    @GetMapping
//    public ResponseEntity<List<NguoiDung>> getAllUsers() {
//        List<NguoiDung> users = nguoiDungService.getAllUsers();
//        return new ResponseEntity<>(users, HttpStatus.OK);
//    }
//
//    // Lấy danh sách người dùng đang hoạt động (trạng thái = false)
//    @GetMapping("/active")
//    public ResponseEntity<List<NguoiDung>> getActiveUsers() {
//        List<NguoiDung> activeUsers = nguoiDungService.getActiveUsers();
//        return new ResponseEntity<>(activeUsers, HttpStatus.OK);
//    }
//
//    // Tạo mới người dùng
//    @PostMapping
//    public ResponseEntity<NguoiDung> createUser(@RequestBody NguoiDung nguoiDung) {
//        NguoiDung createdUser = nguoiDungService.createUser(nguoiDung);
//        return ResponseEntity.ok(createdUser);
//    }
//
//    // Cập nhật thông tin người dùng
//    @PutMapping("/{id}")
//    public ResponseEntity<NguoiDung> updateUser(
//            @PathVariable String id,
//            @RequestBody NguoiDung nguoiDung) {
//        NguoiDung updatedUser = nguoiDungService.updateUser(id, nguoiDung);
//        return ResponseEntity.ok(updatedUser);
//    }
//
//    // Xóa người dùng (cập nhật trạng thái = true)
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
//        nguoiDungService.deleteUser(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    // Chuyển đổi trạng thái người dùng (active/inactive)
//    @PatchMapping("/{id}/toggle-status")
//    public ResponseEntity<NguoiDung> toggleUserStatus(@PathVariable String id) {
//        nguoiDungService.toggleUserStatus(id);
//        return ResponseEntity.ok().build();
//    }
//
//    // Tìm kiếm người dùng theo từ khóa (họ tên hoặc email)
//    @GetMapping("/search")
//    public ResponseEntity<List<NguoiDung>> searchUsers(@RequestParam String keyword) {
//        List<NguoiDung> result = nguoiDungService.searchUsers(keyword);
//        return ResponseEntity.ok(result);
//    }
}
