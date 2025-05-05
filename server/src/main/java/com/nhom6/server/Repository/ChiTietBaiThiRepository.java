package com.nhom6.server.Repository;

import com.nhom6.server.DTO.ChiTietBaiThiDto;
import com.nhom6.server.Model.ChiTietBaiThi;
import com.nhom6.server.Model.ChiTietBaiThiId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietBaiThiRepository extends JpaRepository<ChiTietBaiThi, ChiTietBaiThiId> {
    List<ChiTietBaiThi> findByMaKetQua(String maketqua);

//    @Query("""
//        SELECT new com.example.dto.ChiTietBaiThiFlatDto(
//            ch.maCauHoi, ch.noiDung, ctl.thuTu,
//            ct.maCauTraLoi, ct.noDungCauTraLoi, ct.laDapAn,
//            CASE WHEN ctl.dapAnChon = ct.maCauTraLoi THEN true ELSE false END
//        )
//        FROM ChiTietBaiThi ctl
//        JOIN CauHoi ch ON ctl.maCauHoi = ch.maCauHoi
//        LEFT JOIN CauTraLoi ct ON ch.maCauHoi = ct.maCauHoi
//        WHERE ctl.maKetQua = :maKetQua
//        ORDER BY ctl.thuTu, ct.maCauTraLoi
//    """)
//    List<ChiTietBaiThiFlatDto> getChiTietBaiLamRaw(@Param("maKetQua") String maketqua);

    @Query("SELECT c.maCauHoi, c.dapAnChon FROM ChiTietBaiThi c WHERE c.maKetQua = :maKetQua")
    List<Object[]> findDapAnChonByMaKetQua(@Param("maKetQua") String maKetQua);
}
