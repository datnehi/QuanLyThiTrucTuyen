package com.nhom6.server.Repository;

import com.nhom6.server.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByIdAndMatkhauAndTrangthaiFalse(String id, String matkhau);
}
