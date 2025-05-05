package com.nhom6.server.Services;

import com.nhom6.server.Model.Course;
import com.nhom6.server.Model.PhanMon;
import com.nhom6.server.Model.User;
import com.nhom6.server.Repository.CourseRepository;
import com.nhom6.server.Repository.PhanMonRepository;
import com.nhom6.server.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PhanMonService {

    @Autowired
    private PhanMonRepository phanMonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Lấy danh sách môn học sinh viên đang học
    public List<Course> getMonHocBySinhVien(String id) {
        try {
            List<PhanMon> phanMons = phanMonRepository.findByIdAndTrangThai(id, false);
            return phanMons.stream()
                    .map(phanMon -> {
                        Course course = new Course();
                        course.setMaMonHoc(phanMon.getMaMonHoc());
                        return course;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Thêm phân môn cho sinh viên
    public Map<String, Object> addPhanMon(String id, String mamonhoc) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Kiểm tra người dùng có tồn tại không
            if (!userRepository.existsById(id)) {
                response.put("success", false);
                response.put("message", "Người dùng không tồn tại.");
                return response;
            }

            // Kiểm tra môn học có tồn tại không
            if (!courseRepository.existsByMaMonHoc(mamonhoc)) {
                response.put("success", false);
                response.put("message", "Môn học không tồn tại.");
                return response;
            }

            // Kiểm tra xem sinh viên đã tham gia môn học này chưa
            boolean exists = phanMonRepository.existsByIdAndMaMonHoc(id.trim(), mamonhoc.trim());
            if (exists) {
                response.put("success", false);
                response.put("message", "Sinh viên đã tham gia môn học này rồi.");
                return response;
            }

            // Thêm phân môn cho sinh viên
            PhanMon phanMon = new PhanMon();
            phanMon.setId(id.trim());
            phanMon.setMaMonHoc(mamonhoc.trim());
            phanMon.setTrangThai(false);

            // Lưu phân môn
            phanMonRepository.save(phanMon);

            response.put("success", true);
            response.put("message", "Tham gia học phần thành công!");
            return response;
        }catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Đã có lỗi xảy ra.");
            return response;
        }
    }
}