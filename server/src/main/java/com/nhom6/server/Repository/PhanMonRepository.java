package com.nhom6.server.Repository;

import com.nhom6.server.Model.PhanMon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhanMonRepository extends JpaRepository<PhanMon, Long> {

    List<PhanMon> findByMonHoc_Mamonhoc(String maMonHoc);
    List<PhanMon> findByNguoiDung_IdAndTrangthaiFalse(String id);
    boolean existsByNguoiDung_IdAndMonHoc_Mamonhoc(String id, String mamonhoc);
}
