package com.nhom6.server.Services;

import com.nhom6.server.DTO.ChiTietBaiThiDto;
import com.nhom6.server.DTO.SubmitExamRequest;
import com.nhom6.server.Model.*;
import com.nhom6.server.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChiTietBaiThiService chiTietBaiThiService;

    @Autowired
    private PhanMonRepository phanMonRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ChiTietBaiThiRepository chiTietBaiThiRepository;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final ConcurrentHashMap<String, ScheduledFuture<?>> runningTimers = new ConcurrentHashMap<>();

    private String generateKey(String maKiThi, String id) {
        return maKiThi + "_" + id;
    }

    public void startExam(String maKiThi, String id, int durationMinutes) {
        String key = generateKey(maKiThi, id);
        if (runningTimers.containsKey(key)) return;

        Runnable autoSubmitTask = () -> autoSubmitExam(maKiThi, id, durationMinutes);
        ScheduledFuture<?> scheduledTask = scheduler.schedule(autoSubmitTask, durationMinutes + 1, TimeUnit.MINUTES);

        runningTimers.put(key, scheduledTask);
    }

    private void autoSubmitExam(String maKiThi, String id, int durationMinutes) {
        String key = generateKey(maKiThi, id);
        runningTimers.remove(key);
        submitExam(new SubmitExamRequest(maKiThi, id, durationMinutes));
    }

    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }

    public List<Result> getResultById(String maKiThi) {
        return resultRepository.findByKiThi_Makithi(maKiThi);
    }

    public List<Result> getResultsWithUsers(String maKiThi, String maMonHoc) {
        List<PhanMon> phanMons = phanMonRepository.findByMonHoc_Mamonhoc(maMonHoc);

        List<Result> results = new ArrayList<>();

        for (PhanMon pm : phanMons) {
            User nd = pm.getNguoiDung();
            Optional<Result> kq = resultRepository.findByKiThi_MakithiAndNguoiDung_Id(maKiThi, nd.getId());

            Result result = new Result();
            result.setNguoiDung(nd);

            if (kq.isPresent()) {
                result.setMaketqua(kq.get().getMaketqua());
                result.setDiem(kq.get().getDiem());
                result.setThoigianvaothi(kq.get().getThoigianvaothi());
                result.setThoigianlambai(kq.get().getThoigianlambai());
            }

            results.add(result);
        }

        return results;
    }

    private String generateNextMaKetQua() {
        String lastId = resultRepository.findTopByOrderByMaketquaDesc();
        if (lastId == null) return "0000000001";
        long number = Long.parseLong(lastId);
        return String.format("%010d", number + 1);
    }

    public String createResult(String maKiThi, String id) {
        Optional<Exam> kiThi = examRepository.findById(maKiThi);
        Optional<User> user = userRepository.findById(id);
        if (kiThi.isEmpty() || user.isEmpty()) return null;

        Result kq = new Result();
        kq.setMaketqua(generateNextMaKetQua());
        kq.setKiThi(kiThi.get());
        kq.setNguoiDung(user.get());
        kq.setThoigianvaothi(LocalDateTime.now());

        resultRepository.save(kq);
        return kq.getMaketqua();
    }

    public Result checkKetQua(String maKiThi, String id) {
        return resultRepository.findByKiThi_MakithiAndNguoiDung_Id(maKiThi, id).orElse(null);
    }

    public List<ChiTietBaiThiDto> createExamResult(String maKiThi, String id) {
        Optional<Exam> exam = examRepository.findById(maKiThi);
        if (exam.isEmpty()) return Collections.emptyList();

        Result result = checkKetQua(maKiThi, id);
        String maKetQua = result == null ? createResult(maKiThi, id) : result.getMaketqua();

        if (maKetQua == null) return Collections.emptyList();

        List<String> existingIds = chiTietBaiThiService.checkCauHoi(maKetQua);
        List<Question> cauHoiList;

        if (!existingIds.isEmpty()) {
            cauHoiList = questionRepository.findAllById(existingIds);
        } else {
            cauHoiList = chiTietBaiThiService.randomCauHoi(maKetQua, exam.get().getMonHoc().getMamonhoc(), exam.get().getSocau());
        }

        return chiTietBaiThiService.getCauHoi(cauHoiList, maKetQua);
    }

    public void submitExam(SubmitExamRequest request) {
        String key = generateKey(request.getMaKiThi(), request.getId());
        if (runningTimers.containsKey(key)) {
            runningTimers.get(key).cancel(false);
            runningTimers.remove(key);
        }

        Result result = checkKetQua(request.getMaKiThi(), request.getId());
        if (result == null) throw new IllegalArgumentException("Không tìm thấy kết quả thi.");

        Optional<Exam> exam = examRepository.findById(request.getMaKiThi());
        if (exam.isEmpty()) throw new IllegalArgumentException("Không tìm thấy kỳ thi.");

        List<ChiTietBaiThi> answers = chiTietBaiThiRepository.findByKetQua_Maketqua(result.getMaketqua());

        int correctCount = 0;
        for (ChiTietBaiThi chiTiet : answers) {
            Answer dapAnChon = chiTiet.getDapAnChon();
            if (dapAnChon != null && dapAnChon.isLadapan()) {
                correctCount++;
            }
        }

        Double diem = (10.0D / exam.get().getSocau()) * correctCount;
        result.setDiem(diem);
        result.setSocaudung(correctCount);
        result.setThoigianlambai(request.getTimeUsed());

        resultRepository.save(result);
    }
}