package com.nhom6.server.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    @GetMapping
    public List<String> getUsers() {
        return List.of("User 1", "User 2", "User 3");
    }
}
