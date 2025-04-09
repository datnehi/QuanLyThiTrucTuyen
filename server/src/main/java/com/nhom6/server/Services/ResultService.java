package com.nhom6.server.Services;

import com.nhom6.server.DTO.ResultDto;
import com.nhom6.server.DTO.SubmitExamRequest;
import com.nhom6.server.Model.ChiTietBaiThi;
import com.nhom6.server.Model.Exam;
import com.nhom6.server.Model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

@Service
public class ResultService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ChiTietBaiThiService chiTietBaiThiService;
    @Autowired
    private ExamService examService;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final ConcurrentHashMap<String, ScheduledFuture<?>> runningTimers = new ConcurrentHashMap<>();

    private String generateKey(String maKiThi, String id) {
        return maKiThi + "_" + id;
    }

    public void startExam(String maKiThi, String id, int durationMinutes) {
        try {
            String key = generateKey(maKiThi, id);
            if (runningTimers.containsKey(key)) return;

            Runnable autoSubmitTask = () -> autoSubmitExam(maKiThi, id, durationMinutes);
            ScheduledFuture<?> scheduledTask = scheduler.schedule(autoSubmitTask, durationMinutes + 1, TimeUnit.MINUTES);

            if (scheduledTask != null) runningTimers.put(key, scheduledTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void autoSubmitExam(String maKiThi, String id, int durationMinutes) {
        try {
            String key = generateKey(maKiThi, id);
            if (runningTimers.containsKey(key)) {
                runningTimers.remove(key);
                submitExam(new SubmitExamRequest(maKiThi, id, durationMinutes));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Result> getAllResults() {
        try {
            String sql = "SELECT * FROM ketqua";
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Result.class));
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Result> getResultById(String maKiThi) {
        try {
            String sql = "SELECT * FROM ketqua WHERE maKiThi = ?";
            return jdbcTemplate.query(sql, new Object[]{maKiThi}, new BeanPropertyRowMapper<>(Result.class));
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<ResultDto> getResultsWithUsers(String maKiThi, String maMonHoc) {
        try {
            String sql = """
                SELECT * FROM phanmon pm
                JOIN nguoidung nd ON pm.id = nd.id
                LEFT JOIN ketqua kq ON pm.id = kq.id AND kq.maKiThi = ?
                WHERE pm.maMonHoc = ?
            """;

            return jdbcTemplate.query(sql, new Object[]{maKiThi, maMonHoc}, (rs, rowNum) -> {
                ResultDto result = new ResultDto();
                result.setMaKetQua(rs.getString("maketqua"));
                result.setId(rs.getString("id"));
                result.setHoten(rs.getString("hoten"));
                result.setDiem(rs.getObject("diem") != null ? rs.getFloat("diem") : null);
                result.setThoiGianVaoThi(rs.getTimestamp("thoigianvaothi") != null
                        ? rs.getTimestamp("thoigianvaothi").toLocalDateTime()
                        : null);
                result.setThoiGianLamBai(rs.getObject("thoigianlambai") != null ? rs.getFloat("thoigianlambai") : null);
                return result;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private String generateNextMaKetQua() {
        try {
            String sql = "SELECT TOP 1 maketqua FROM ketqua ORDER BY maketqua DESC";
            String lastMaKetQua = jdbcTemplate.queryForObject(sql, String.class);
            if (lastMaKetQua == null || lastMaKetQua.isEmpty()) return "0000000001";

            long number = Long.parseLong(lastMaKetQua);
            return String.format("%010d", ++number);
        } catch (Exception e) {
            e.printStackTrace();
            return "0000000001";
        }
    }

    public String createResult(String maKiThi, String id) {
        try {
            String maKetQua = generateNextMaKetQua();
            String sql = """
                INSERT INTO ketqua (maketqua, makithi, id, diem, thoigianvaothi, thoigianlambai, socaudung)
                VALUES (?, ?, ?, null, GETDATE(), null, null)
            """;
            jdbcTemplate.update(sql, maKetQua, maKiThi, id);
            return maKetQua;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Result checkKetQua(String maKiThi, String id) {
        try {
            String sql = "SELECT * FROM ketqua WHERE maKiThi = ? AND id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{maKiThi, id}, new BeanPropertyRowMapper<>(Result.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ChiTietBaiThi> createExamResult(String maKiThi, String id) {
        try {
            Optional<Exam> kiThi = examService.getExamByMa(maKiThi);
            if (kiThi.isEmpty()) return Collections.emptyList();

            Result existingKetQua = checkKetQua(maKiThi, id);
            String maKetQua = existingKetQua == null ? createResult(maKiThi, id) : existingKetQua.getMaKetQua();
            if (maKetQua == null) return Collections.emptyList();

            List<String> existingCauHoiIds = chiTietBaiThiService.checkCauHoi(maKetQua);
            List<Map<String, Object>> cauHoiList;

            if (!existingCauHoiIds.isEmpty()) {
                String placeholders = String.join(",", Collections.nCopies(existingCauHoiIds.size(), "?"));
                String selectCauHoiSql = "SELECT * FROM cauhoi WHERE maCauHoi IN (" + placeholders + ")";
                cauHoiList = jdbcTemplate.queryForList(selectCauHoiSql, existingCauHoiIds.toArray());
            } else {
                cauHoiList = chiTietBaiThiService.randomCauHoi(maKetQua, kiThi.get().getMaMonHoc(), kiThi.get().getSoCau());
            }

            return chiTietBaiThiService.getCauHoi(cauHoiList, maKetQua);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void submitExam(SubmitExamRequest request) {
        try {
            String key = generateKey(request.getMaKiThi(), request.getId());
            if (runningTimers.containsKey(key)) {
                runningTimers.get(key).cancel(false);
                runningTimers.remove(key);
            }

            Result result = checkKetQua(request.getMaKiThi(), request.getId());
            if (result == null) throw new IllegalArgumentException("Không tìm thấy kết quả thi.");

            Optional<Exam> exam = examService.getExamByMa(request.getMaKiThi());
            if (exam.isEmpty()) throw new IllegalArgumentException("Không tìm thấy kỳ thi.");

            String selectQuery = "SELECT macauhoi, dapanchon FROM chitietde WHERE maketqua = ?";
            List<Map<String, Object>> answers = jdbcTemplate.queryForList(selectQuery, result.getMaKetQua());

            int correctCount = 0;

            for (Map<String, Object> answer : answers) {
                String dapAnChon = (String) answer.get("dapanchon");
                if (dapAnChon == null || dapAnChon.isEmpty()) continue;

                String checkQuery = "SELECT COUNT(*) FROM cautraloi WHERE macautraloi = ? AND ladapan = 1";
                int isCorrect = jdbcTemplate.queryForObject(checkQuery, Integer.class, dapAnChon);
                correctCount += isCorrect;
            }

            float diem = (10.0f / exam.get().getSoCau()) * correctCount;
            String updateKetQua = "UPDATE ketqua SET diem = ?, socaudung = ?, thoigianlambai = ? WHERE maketqua = ?";
            jdbcTemplate.update(updateKetQua, diem, correctCount, request.getTimeUsed(), result.getMaKetQua());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
