package com.nhom6.server.Repository;

import com.nhom6.server.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByIdAndMatKhauAndTrangThaiFalse(String id, String matkhau);

    boolean existsById(String id);

    List<User> findByTrangThai(boolean trangThai);

    User findTopByOrderByIdDesc();

    @Query("SELECT u FROM User u JOIN PhanMon p ON u.id = p.id WHERE p.maMonHoc = :maMonHoc")
    List<User> findUsersByMaMonHoc(@Param("maMonHoc") String maMonHoc);
}
