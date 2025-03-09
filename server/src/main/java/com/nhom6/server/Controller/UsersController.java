package com.nhom6.server.Controller;

import com.nhom6.server.Services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class UsersController {
    @Autowired
    private UserService userService;
    // API xử lý đăng nhập
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest, HttpSession session) {
        String id = loginRequest.get("id");
        String matkhau = loginRequest.get("matkhau");

        Map<String, Object> user = userService.login(id, matkhau);

        if (user != null) {
            String token = UUID.randomUUID().toString();
            session.setAttribute("userId", user.get("id"));
            session.setAttribute("userToken", token);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Đăng nhập thành công");
            response.put("userToken", token);
            response.put("user" , user);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Sai email hoặc mật khẩu hoặc tài khoản bị khóa"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        // Xoá session
        session.invalidate();

        // Phản hồi thành công
        Map<String, String> response = new HashMap<>();
        response.put("message", "Đăng xuất thành công");

        return ResponseEntity.ok(response);
    }
}
