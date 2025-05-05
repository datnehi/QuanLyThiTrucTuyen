package com.nhom6.server.Services;

import com.nhom6.server.Model.Exam;
import com.nhom6.server.Repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    // Lấy tất cả kỳ thi
    public List<Exam> getAllExams() {
        return examRepository.findByTrangthaiOrderByThoigiantaoDesc(false);
    }

    // Lấy kỳ thi theo maKiThi
    public Optional<Exam> getExamByMa(String maKiThi) {
        return examRepository.findByMakithiAndTrangthai(maKiThi, false);
    }

    // Lấy kỳ thi theo maMonHoc
    public List<Exam> getExamsByName(String maMonHoc) {
        return examRepository.findByMonHoc_MamonhocAndTrangthaiOrderByThoigiantaoDesc(maMonHoc, false);
    }

    // Lấy kỳ thi theo id (sinh viên)
    public List<Exam> getExamsById(String id) {
        return examRepository.findByPhanMonIdAndTrangThaiIsFalseOrderByThoiGianTaoDesc(id);
    }

    // Tạo mã kỳ thi tiếp theo
    public String generateNextMaKiThi() {
        Optional<Exam> lastKiThiOpt = examRepository.findTopByOrderByMaKiThiDesc();

        String lastMaKiThi = lastKiThiOpt.map(Exam::getMakithi).orElse(null);

        if (lastMaKiThi == null || lastMaKiThi.isEmpty()) {
            return "0000000001";
        }

        long number = Long.parseLong(lastMaKiThi);
        return String.format("%010d", ++number);
    }

    // Tạo kỳ thi mới
    public Optional<Exam> createExam(Exam exam) {
        try {
            String maKiThi = generateNextMaKiThi();
            exam.setMakithi(maKiThi);
            return Optional.of(examRepository.save(exam));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Cập nhật kỳ thi
    public Optional<Exam> updateExam(String maKiThi, Exam examDetails) {
        Optional<Exam> existingExam = examRepository.findById(maKiThi);
        if (existingExam.isPresent()) {
            Exam exam = existingExam.get();
            exam.setTenkithi(examDetails.getTenkithi());
            exam.setThoigiantao(examDetails.getThoigiantao());
            exam.setThoigianthi(examDetails.getThoigianthi());
            exam.setThoigianbatdau(examDetails.getThoigianbatdau());
            exam.setThoigianketthuc(examDetails.getThoigianketthuc());
            exam.setXemdiem(examDetails.isXemdiem());
            exam.setXemdapan(examDetails.isXemdapan());
            exam.setHienthibailam(examDetails.isHienthibailam());
            exam.setSocau(examDetails.getSocau());
            exam.setTrangthai(examDetails.isTrangthai());

            return Optional.of(examRepository.save(exam));
        }
        return Optional.empty();
    }

    // Xóa kỳ thi (ẩn)
    public boolean deleteExam(String maKiThi) {
        Optional<Exam> exam = examRepository.findById(maKiThi);
        if (exam.isPresent()) {
            Exam e = exam.get();
            e.setTrangthai(true);  // Cập nhật trạng thái thành "ẩn"
            examRepository.save(e);
            return true;
        }
        return false;
    }
}
