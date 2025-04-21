package com.nhom6.server.Controller;

import com.nhom6.server.Services.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

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

            Optional<Map<String, Object>> user = accountService.login(id, matkhau);

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
            List<Account> accounts = accountService.getAll();
            if (accounts.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body(Map.of("message", "Không có tài khoản nào"));
            }

            return ResponseEntity.ok(Map.of(
                    "message", "Lấy danh sách tài khoản thành công",
                    "data", accounts
            ));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Đã xảy ra lỗi khi lấy danh sách tài khoản"));
        }
    }
}
