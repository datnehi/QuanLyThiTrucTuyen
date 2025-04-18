package com.nhom6.server.Services;

import com.nhom6.server.DTO.AnswerDto;
import com.nhom6.server.Model.ChiTietBaiThi;
import com.nhom6.server.Model.DapAn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChiTietBaiThiService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ChiTietBaiThi> getChiTietBaiLam(String maketqua) {
        try {
            String sql = """
                SELECT 
                    ch.macauhoi, 
                    ch.noidung AS cauhoi,
                    ctl.maketqua,
                    ctl.dapanchon AS dapan_chon,
                    ctl.thutu,
                    ct.macautraloi,
                    ct.noidung AS dapan,
                    ct.ladapan
                FROM chitietde ctl
                JOIN cauhoi ch ON ctl.macauhoi = ch.macauhoi
                JOIN cautraloi ct ON ch.macauhoi = ct.macauhoi
                WHERE ctl.maketqua = ? 
                ORDER BY ctl.thutu, ct.macautraloi;
            """;

            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, maketqua);
            Map<String, ChiTietBaiThi> cauHoiMap = new LinkedHashMap<>();

            for (Map<String, Object> row : rows) {
                String macauhoi = (String) row.get("macauhoi");
                String noidung = (String) row.get("cauhoi");
                int thutu = (int) row.get("thutu");

                cauHoiMap.putIfAbsent(macauhoi, new ChiTietBaiThi(macauhoi, noidung, thutu));
                ChiTietBaiThi cauHoi = cauHoiMap.get(macauhoi);

                String macautraloi = (String) row.get("macautraloi");
                String noidungDapan = (String) row.get("dapan");
                boolean ladapan = (boolean) row.get("ladapan");
                boolean laDapAnChon = row.get("dapan_chon") != null && macautraloi.equals(row.get("dapan_chon"));

                cauHoi.addDapAn(new DapAn(macautraloi, noidungDapan, ladapan, laDapAnChon));
            }

            return new ArrayList<>(cauHoiMap.values());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<String> checkCauHoi(String maKetQua) {
        try {
            String sql = "SELECT maCauHoi FROM chitietde WHERE maKetQua = ?";
            return jdbcTemplate.queryForList(sql, String.class, maKetQua);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Map<String, Object>> randomCauHoi(String maKetQua, String maMonHoc, int soCau) {
        try {
            String sql = "SELECT TOP (?) * FROM cauhoi WHERE maMonHoc = ? AND trangthai = 0 ORDER BY NEWID()";
            List<Map<String, Object>> cauHoiList = jdbcTemplate.queryForList(sql, soCau, maMonHoc);

            String insertSql = "INSERT INTO chitietde (maKetQua, maCauHoi, thuTu) VALUES (?, ?, ?)";
            int index = 1;
            for (Map<String, Object> cauHoi : cauHoiList) {
                jdbcTemplate.update(insertSql, maKetQua, cauHoi.get("maCauHoi"), index++);
            }
            return cauHoiList;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<ChiTietBaiThi> getCauHoi(List<Map<String, Object>> cauHoiList, String maKetQua) {
        List<ChiTietBaiThi> danhSachCauHoi = new ArrayList<>();
        try {
            String sqlDapAnChon = "SELECT macauhoi, dapanchon FROM chitietde WHERE maketqua = ?";
            List<Map<String, Object>> dapAnChonList = jdbcTemplate.queryForList(sqlDapAnChon, maKetQua);

            Map<String, String> dapAnChonMap = new HashMap<>();
            for (Map<String, Object> row : dapAnChonList) {
                dapAnChonMap.put((String) row.get("macauhoi"), (String) row.get("dapanchon"));
            }

            for (Map<String, Object> cauHoi : cauHoiList) {
                String maCauHoi = (String) cauHoi.get("macauhoi");
                String noiDungCauHoi = (String) cauHoi.get("noidung");

                String sqlDapAn = "SELECT * FROM cautraloi WHERE maCauHoi = ?";
                List<DapAn> dapAns = jdbcTemplate.query(sqlDapAn, (rs, rowNum) -> new DapAn(
                        rs.getString("maCauTraLoi"),
                        rs.getString("noiDung"),
                        rs.getBoolean("laDapAn"),
                        rs.getString("maCauTraLoi").equals(dapAnChonMap.get(maCauHoi))
                ), maCauHoi);

                danhSachCauHoi.add(new ChiTietBaiThi(maCauHoi, noiDungCauHoi, dapAns, 0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return danhSachCauHoi;
    }

    public void saveAnswer(AnswerDto answer) {
        try {
            String sql = "UPDATE chitietde SET dapanchon = ? WHERE maketqua = ? AND macauhoi = ?";
            jdbcTemplate.update(sql,
                    answer.getDapanchon(),
                    answer.getMaketqua(),
                    answer.getMacauhoi()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
