package com.nhom6.server.Services;

import com.nhom6.server.DTO.ChiTietBaiThiDto;
import com.nhom6.server.DTO.ResultDto;
import com.nhom6.server.DTO.SubmitExamRequest;
import com.nhom6.server.Model.*;
import com.nhom6.server.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class ResultService {

    @Autowired
    private ChiTietBaiThiService chiTietBaiThiService;

    @Autowired
    private ExamService examService;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository nguoiDungRepository;

    @Autowired
    private PhanMonRepository phanMonRepository;

    @Autowired
    private ChiTietBaiThiRepository chiTietBaiThiRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final ConcurrentHashMap<String, ScheduledFuture<?>> runningTimers = new ConcurrentHashMap<>();

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
        return resultRepository.findAll();
    }

    public List<Result> getResultById(String maKiThi) {
        return resultRepository.findByMaKiThi(maKiThi);
    }

    public List<ResultDto> getResultsWithUsers(String maKiThi, String maMonHoc) {
        try {
            List<Object[]> rows = resultRepository.findResultsWithUsers(maKiThi, maMonHoc);
            return rows.stream().map(row -> {
                ResultDto dto = new ResultDto();
                dto.setMaKetQua((String) row[0]);
                dto.setId((String) row[1]);
                dto.setHoten((String) row[2]);
                dto.setDiem(row[3] != null ? Double.valueOf(((Number) row[3]).doubleValue()) : null);
                dto.setThoiGianVaoThi(row[4] != null ? ((Timestamp) row[4]).toLocalDateTime() : null);
                dto.setThoiGianLamBai(row[5] != null ? ((Number) row[5]).intValue() : null);
                return dto;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    private String generateNextMaKetQua() {
        try {
            String lastMaKetQua = resultRepository.findLatestMaKetQua().orElse(null);
            if (lastMaKetQua == null || lastMaKetQua.isEmpty()) {
                return "0000000001";
            }
            long number = Long.parseLong(lastMaKetQua);
            return String.format("%010d", number + 1);
        } catch (Exception e) {
            e.printStackTrace();
            return "0000000001";
        }
    }

    @Transactional
    public String createResult(String maKiThi, String id) {
        try {
            String maKetQua = generateNextMaKetQua(); // dùng phiên bản đã chuyển ở trên
            Result ketQua = new Result();
            ketQua.setMaKetQua(maKetQua);
            ketQua.setMaKiThi(maKiThi);
            ketQua.setId(id);
            ketQua.setDiem(null);
            ketQua.setThoiGianVaoThi(LocalDateTime.now());
            ketQua.setThoiGianLamBai(null);
            ketQua.setSoCauDung(null);

            resultRepository.save(ketQua);
            return maKetQua;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Result checkKetQua(String maKiThi, String id) {
        try {
            return resultRepository.findByMaKiThiAndId(maKiThi, id).orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ChiTietBaiThiDto> createExamResult(String maKiThi, String id) {
        try {
            Optional<Exam> optionalExam = examService.getExamByMa(maKiThi);
            if (optionalExam.isEmpty()) return Collections.emptyList();

            Exam exam = optionalExam.get();

            Result existingResult = checkKetQua(maKiThi, id);
            String maKetQua = existingResult == null
                    ? createResult(maKiThi, id)
                    : existingResult.getMaKetQua();

            if (maKetQua == null) return Collections.emptyList();

            // Lấy danh sách mã câu hỏi đã tồn tại trong chi tiết bài thi
            List<ChiTietBaiThi> existingChiTietList = chiTietBaiThiRepository.findByMaKetQua(maKetQua);
            List<String> maCauHoiList = existingChiTietList.stream()
                    .map(ChiTietBaiThi::getMaCauHoi)  // Truyền maCauHoi từ DTO
                    .collect(Collectors.toList());
            List<Question> cauHoiList;

            if (!existingChiTietList.isEmpty()) {
                cauHoiList = questionService.getQuestionsByMaCauHoiList(maCauHoiList);
            } else {
                cauHoiList = chiTietBaiThiService.randomCauHoi(maKetQua, exam.getMaMonHoc(), exam.getSoCau());
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

            Result result = resultRepository.findByMaKiThiAndId(request.getMaKiThi(), request.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy kết quả thi."));

            Exam exam = examRepository.findByMaKiThi(request.getMaKiThi())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy kỳ thi."));

            List<Object[]> answerData = chiTietBaiThiRepository.findDapAnChonByMaKetQua(result.getMaKetQua());

            int correctCount = 0;
            for (Object[] row : answerData) {
                String dapAnChon = (String) row[1];
                if (dapAnChon == null || dapAnChon.isEmpty()) continue;

                boolean isCorrect = answerRepository.existsByMaCauTraLoiAndLaDapAnTrue(dapAnChon);
                if (isCorrect) correctCount++;
            }

            Double diem = (10.0 / exam.getSoCau()) * correctCount;
            result.setDiem(diem);
            result.setSoCauDung(correctCount);
            result.setThoiGianLamBai(request.getTimeUsed());
            resultRepository.save(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateKey(String maKiThi, String id) {
        return maKiThi + ":" + id;
    }
}