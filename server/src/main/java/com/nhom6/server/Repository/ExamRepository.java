package com.nhom6.server.Repository;
import com.nhom6.server.Model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, String> {

    List<Exam> findByTrangthaiOrderByThoigiantaoDesc(boolean trangThai);

    Optional<Exam> findByMakithiAndTrangthai(String maKiThi, boolean trangThai);

    List<Exam> findByMonHoc_MamonhocAndTrangthaiOrderByThoigiantaoDesc(String maMonHoc, boolean trangThai);

    @Query("""
        SELECT e FROM Exam e
        JOIN e.monHoc m
        JOIN m.phanMons pm
        WHERE pm.id = :phanMonId AND e.trangthai = false
        ORDER BY e.thoigiantao DESC
    """)
    List<Exam> findByPhanMonIdAndTrangThaiIsFalseOrderByThoiGianTaoDesc(@Param("phanMonId") String phanMonId);

    Optional<Exam> findTopByOrderByMaKiThiDesc();
}
