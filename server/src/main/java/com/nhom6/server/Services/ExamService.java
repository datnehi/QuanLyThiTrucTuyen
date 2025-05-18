package com.nhom6.server.Services;

import com.nhom6.server.Model.Exam;
import com.nhom6.server.Repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    // Lấy tất cả kỳ thi
    public List<Exam> getAllExams() {
        try {
            return examRepository.findByTrangThaiFalseOrderByThoiGianTaoDesc();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Lấy kỳ thi theo maKiThi
    public Optional<Exam> getExamByMa(String maKiThi) {
        try {
            return examRepository.findByMaKiThi(maKiThi);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Lấy kỳ thi theo maMonHoc
    public List<Exam> getExamsByMaMonHoc(String maMonHoc) {
        try {
            return examRepository.findByMaMonHocAndTrangThaiFalseOrderByThoiGianTaoDesc(maMonHoc);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Lấy kỳ thi theo id (sinh viên)
    public List<Exam> getExamsById(String id) {
        try {
            return examRepository.findExamsByPhanMonIdAndTrangThai(id);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Tạo mã kỳ thi tiếp theo
    private String generateNextMaKiThi() {
        Optional<Exam> lastExam = examRepository.findTopByOrderByMaKiThiDesc();
        if (lastExam.isPresent()) {
            String lastMaKiThi = lastExam.get().getMaKiThi();
            long number = Long.parseLong(lastMaKiThi);
            return String.format("%010d", ++number);
        }
        return "0000000001";
    }

    // Tạo kỳ thi mới
    public Optional<Exam> createExam(Exam exam) {
        try {
            String maKiThi = generateNextMaKiThi();
            exam.setMaKiThi(maKiThi);

            return Optional.of(examRepository.save(exam));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Cập nhật kỳ thi
    public Optional<Exam> updateExam(String maKiThi, Exam examDetails) {
        try {
            Optional<Exam> existingExam = examRepository.findById(maKiThi);
            if (existingExam.isPresent()) {
                Exam exam = existingExam.get();
                exam.setTenKiThi(examDetails.getTenKiThi());
                exam.setThoiGianTao(examDetails.getThoiGianTao());
                exam.setThoiGianThi(examDetails.getThoiGianThi());
                exam.setThoiGianBatDau(examDetails.getThoiGianBatDau());
                exam.setThoiGianKetThuc(examDetails.getThoiGianKetThuc());
                exam.setXemDiem(examDetails.isXemDiem());
                exam.setXemDapAn(examDetails.isXemDapAn());
                exam.setHienThiBaiLam(examDetails.isHienThiBaiLam());
                exam.setSoCau(examDetails.getSoCau());
                exam.setTrangThai(examDetails.isTrangThai());

                return Optional.of(examRepository.save(exam));
            }
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Xóa kỳ thi (ẩn)
    public boolean deleteExam(String maKiThi) {
        try {
            Optional<Exam> exam = examRepository.findById(maKiThi);
            if (exam.isPresent()) {
                exam.get().setTrangThai(true); // Đánh dấu là đã xóa
                examRepository.save(exam.get());
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}