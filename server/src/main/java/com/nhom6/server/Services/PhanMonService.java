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
        List<PhanMon> phanMons = phanMonRepository.findByNguoiDung_IdAndTrangthaiFalse(id);
        List<Course> monHocs = new ArrayList<>();
        for (PhanMon pm : phanMons) {
            monHocs.add(pm.getMonHoc());
        }
        return monHocs;
    }

    // Thêm phân môn cho sinh viên
    public Map<String, Object> addPhanMon(String id, String mamonhoc) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> nguoiDungOpt = userRepository.findById(id.trim());
        if (nguoiDungOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Người dùng không tồn tại.");
            return response;
        }

        Optional<Course> monHocOpt = courseRepository.findById(mamonhoc.trim());
        if (monHocOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Môn học không tồn tại.");
            return response;
        }

        boolean exists = phanMonRepository.existsByNguoiDung_IdAndMonHoc_Mamonhoc(id.trim(), mamonhoc.trim());
        if (exists) {
            response.put("success", false);
            response.put("message", "Sinh viên đã tham gia môn học này rồi.");
            return response;
        }

        PhanMon pm = new PhanMon();
        pm.setNguoiDung(nguoiDungOpt.get());
        pm.setMonHoc(monHocOpt.get());
        pm.setTrangthai(false);

        phanMonRepository.save(pm);

        response.put("success", true);
        response.put("message", "Tham gia học phần thành công!");
        return response;
    }
}