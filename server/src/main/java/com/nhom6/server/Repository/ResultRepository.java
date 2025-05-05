package com.nhom6.server.Repository;

import com.nhom6.server.DTO.ResultDto;
import com.nhom6.server.Model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface ResultRepository extends JpaRepository<Result, String> {
    Optional<Result> findByMaKiThiAndId(String maKiThi, String id);

    List<Result> findByMaKiThi(String maKiThi);

    @Query(value = """
        SELECT 
            kq.maketqua, pm.id, nd.hoten, 
            kq.diem, kq.thoigianvaothi, kq.thoigianlambai
        FROM phanmon pm
        JOIN nguoidung nd ON pm.id = nd.id
        LEFT JOIN ketqua kq ON pm.id = kq.id AND kq.makithi = :maKiThi
        WHERE pm.mamonhoc = :maMonHoc
        """, nativeQuery = true)
    List<Object[]> findResultsWithUsers(@Param("maKiThi") String maKiThi, @Param("maMonHoc") String maMonHoc);

    @Query(value = "SELECT TOP 1 maketqua FROM ketqua ORDER BY maketqua DESC", nativeQuery = true)
    Optional<String> findLatestMaKetQua();
}
