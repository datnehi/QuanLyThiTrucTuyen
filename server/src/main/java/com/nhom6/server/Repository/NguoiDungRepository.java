package com.nhom6.server.Repository;

import com.nhom6.server.Model.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, String> {
    List<NguoiDung> findByTrangThai(boolean trangThai);
    NguoiDung findTopByOrderByIdDesc();
    List<NguoiDung> findByHoTenContainingIgnoreCaseOrEmailContainingIgnoreCase(String hoTen, String email);
}