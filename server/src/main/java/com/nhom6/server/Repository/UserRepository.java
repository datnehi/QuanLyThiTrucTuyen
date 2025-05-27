package com.nhom6.server.Repository;

import com.nhom6.server.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByIdAndMatKhauAndTrangThaiFalse(String id, String matkhau);
    boolean existsById(String id);
    List<User> findByTrangThai(boolean trangThai);
    User findTopByOrderByIdDesc();
    List<User> findByHoTenContainingIgnoreCaseOrEmailContainingIgnoreCase(String hoTen, String email);
}
