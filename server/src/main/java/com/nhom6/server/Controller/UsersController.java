package com.nhom6.server.Controller;

import com.nhom6.server.Model.LoginRequest;
import com.nhom6.server.Model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UsersController {
    // API xử lý đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        // Dummy check for admin credentials
        if ("admin".equals(request.getUsername()) && "123456".equals(request.getPassword())) {
            User user = new User(request.getUsername(), request.getPassword(), "ADMIN");
            return ResponseEntity.ok(user);
        }

        // Trả về lỗi nếu thông tin không đúng
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid credentials");
    }
}
