package com.nhom6.server.Repository;

import com.nhom6.server.Model.PhanMon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhanMonRepository extends JpaRepository<PhanMon, Long> {

    List<PhanMon> findByMaMonHoc(String maMonHoc);
    List<PhanMon> findByIdAndTrangThai(String id, boolean trangThai);
    boolean existsByIdAndMaMonHoc(String id, String mamonhoc);
}
