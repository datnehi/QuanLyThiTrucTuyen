package com.nhom6.server.Services;


import com.nhom6.server.Model.Course;
import com.nhom6.server.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;

    // Lấy tất cả các môn học có trạng thái là false (trangthai = 0)
    public List<Course> getAllMonHoc() {
        return courseRepository.findByTrangThaiFalse();
    }

    // Lấy môn học theo mã môn học và trạng thái false
    public Course getMonHocById(String maMonHoc) {
        Optional<Course> course = courseRepository.findByMaMonHocAndTrangThaiFalse(maMonHoc);
        return course.orElseThrow(() -> new RuntimeException("Môn học không tồn tại!"));
    }

    public List<Course> getAllSubjects() {
        return courseRepository.findAll();
    }

    public List<Course> getActiveSubjects() {
        return courseRepository.findByTrangThai(false); // Trạng thái = 0 (false)
    }

    public Course createSubject(Course subject) {
        // Mặc định trạng thái là 0 (false) khi tạo mới
        subject.setTrangThai(false);

        // Nếu đã có mã môn học, kiểm tra xem có tồn tại không
        if (subject.getMaMonHoc() != null && !subject.getMaMonHoc().isEmpty()) {
            if (courseRepository.existsById(subject.getMaMonHoc())) {
                throw new RuntimeException("Mã môn học đã tồn tại");
            }
            return courseRepository.save(subject);
        }

        // Generate ID mới
        try {
            Course lastSubject = courseRepository.findTopByOrderByMaMonHocDesc();
            if (lastSubject != null) {
                try {
                    int lastNumber = Integer.parseInt(lastSubject.getMaMonHoc());
                    String newMamonhoc = String.format("%05d", lastNumber + 1);
                    subject.setMaMonHoc(newMamonhoc);
                } catch (NumberFormatException e) {
                    // Nếu mã không phải số, tạo UUID
                    String uniqueID = UUID.randomUUID().toString().replace("-", "").substring(0, 5);
                    subject.setMaMonHoc(uniqueID);
                }
            } else {
                subject.setMaMonHoc("00001");
            }
            return courseRepository.save(subject);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo mã môn học mới", e);
        }
    }

    public Course updateSubject(String mamonhoc, Course subject) {
        // Lấy trạng thái hiện tại của môn học
        Course existingSubject = courseRepository.findById(mamonhoc).orElse(null);
        if (existingSubject != null) {
            subject.setTrangThai(existingSubject.isTrangThai()); // Giữ nguyên trạng thái
        }
        subject.setMaMonHoc(mamonhoc);
        return courseRepository.save(subject);
    }

    public void deleteSubject(String mamonhoc) {
        // Thay vì xóa, cập nhật trạng thái = 1 (true)
        Course subject = courseRepository.findById(mamonhoc).orElse(null);
        if (subject != null) {
            subject.setTrangThai(true);
            courseRepository.save(subject);
        }
    }

    public void toggleSubjectStatus(String mamonhoc) {
        Course subject = courseRepository.findById(mamonhoc).orElse(null);
        if (subject != null) {
            subject.setTrangThai(!subject.isTrangThai());
            courseRepository.save(subject);
        }
    }

    public List<Course> searchSubjects(String keyword) {
        return courseRepository.findByTenMonHocContainingIgnoreCase(keyword);
    }
}
