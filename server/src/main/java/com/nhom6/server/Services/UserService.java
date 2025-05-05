package com.nhom6.server.Services;

import com.nhom6.server.Model.User;
import com.nhom6.server.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> login(String id, String matkhau) {
        return userRepository.findByIdAndMatkhauAndTrangthaiFalse(id, matkhau);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}