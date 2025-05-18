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

    List<Exam> findByTrangThaiFalseOrderByThoiGianTaoDesc();

    Optional<Exam> findByMaKiThi(String maKiThi);

    List<Exam> findByMaMonHocAndTrangThaiFalseOrderByThoiGianTaoDesc(String maMonHoc);

    @Query(value = """
        SELECT k.makithi as makithi,
               k.mamonhoc as mamonhoc,
               k.tenkithi as tenkithi,
               k.thoigiantao as thoigiantao,
               k.thoigianthi as thoigianthi,
               k.thoigianbatdau as thoigianbatdau,
               k.thoigianketthuc as thoigianketthuc,
               k.xemdiem as xemdiem,
               k.xemdapan as xemdapan,
               k.hienthibailam as hienthibailam,
               k.socau as socau,
               k.trangthai as trangthai
        FROM kithi k
        JOIN phanmon pm ON k.mamonhoc = pm.mamonhoc
        JOIN monhoc mh ON k.mamonhoc = mh.mamonhoc
        WHERE pm.id = :id AND k.trangthai = 0
        ORDER BY k.thoigiantao DESC
        """, nativeQuery = true)
    List<Exam> findExamsByPhanMonIdAndTrangThai(@Param("id") String id);

    Optional<Exam> findTopByOrderByMaKiThiDesc();
}
