package com.nhom6.server.Repository;

import com.nhom6.server.Model.ChiTietBaiThi;
import com.nhom6.server.Model.ChiTietBaiThiId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietBaiThiRepository extends JpaRepository<ChiTietBaiThi, ChiTietBaiThiId> {
    List<ChiTietBaiThi> findByKetQua_Maketqua(String maketqua);
}
