package com.nhom6.server.Services;

import com.nhom6.server.Repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public List<Subject> getActiveSubjects() {
        return subjectRepository.findByTrangthai(false); // Trạng thái = 0 (false)
    }

    @Override
    public Subject createSubject(Subject subject) {
        // Mặc định trạng thái là 0 (false) khi tạo mới
        subject.setTrangthai(false);

        // Nếu đã có mã môn học, kiểm tra xem có tồn tại không
        if (subject.getMamonhoc() != null && !subject.getMamonhoc().isEmpty()) {
            if (subjectRepository.existsById(subject.getMamonhoc())) {
                throw new RuntimeException("Mã môn học đã tồn tại");
            }
            return subjectRepository.save(subject);
        }

        // Generate ID mới
        try {
            Subject lastSubject = subjectRepository.findTopByOrderByMamonhocDesc();
            if (lastSubject != null) {
                try {
                    int lastNumber = Integer.parseInt(lastSubject.getMamonhoc());
                    String newMamonhoc = String.format("%05d", lastNumber + 1);
                    subject.setMamonhoc(newMamonhoc);
                } catch (NumberFormatException e) {
                    // Nếu mã không phải số, tạo UUID
                    String uniqueID = UUID.randomUUID().toString().replace("-", "").substring(0, 5);
                    subject.setMamonhoc(uniqueID);
                }
            } else {
                subject.setMamonhoc("00001");
            }
            return subjectRepository.save(subject);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo mã môn học mới", e);
        }
    }

    @Override
    public Subject updateSubject(String mamonhoc, Subject subject) {
        // Lấy trạng thái hiện tại của môn học
        Subject existingSubject = subjectRepository.findById(mamonhoc).orElse(null);
        if (existingSubject != null) {
            subject.setTrangthai(existingSubject.getTrangthai()); // Giữ nguyên trạng thái
        }
        subject.setMamonhoc(mamonhoc);
        return subjectRepository.save(subject);
    }

    @Override
    public void deleteSubject(String mamonhoc) {
        // Thay vì xóa, cập nhật trạng thái = 1 (true)
        Subject subject = subjectRepository.findById(mamonhoc).orElse(null);
        if (subject != null) {
            subject.setTrangthai(true);
            subjectRepository.save(subject);
        }
    }

    @Override
    public void toggleSubjectStatus(String mamonhoc) {
        Subject subject = subjectRepository.findById(mamonhoc).orElse(null);
        if (subject != null) {
            subject.setTrangthai(!subject.getTrangthai());
            subjectRepository.save(subject);
        }
    }

    @Override
    public List<Subject> searchSubjects(String keyword) {
        return subjectRepository.findByTenmonhocContainingIgnoreCase(keyword);
    }
}