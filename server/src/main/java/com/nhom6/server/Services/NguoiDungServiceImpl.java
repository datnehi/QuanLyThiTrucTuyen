package com.nhom6.server.Services;

import com.nhom6.server.Model.NguoiDung;
import com.nhom6.server.Repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NguoiDungServiceImpl implements NguoiDungService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Override
    public List<NguoiDung> getAllUsers() {
        return nguoiDungRepository.findAll();
    }
    @Override
    public List<NguoiDung> getActiveUsers() {
        return nguoiDungRepository.findByTrangThai(false); // Trạng thái = 0 (false) - active
    }
    @Override
    public NguoiDung createUser(NguoiDung nguoiDung) {
        // Mặc định trạng thái là 0 (false) khi tạo mới
        nguoiDung.setTrangThai(false);

        // Nếu đã có ID, kiểm tra xem có tồn tại không
        if (nguoiDung.getId() != null && !nguoiDung.getId().isEmpty()) {
            if (nguoiDungRepository.existsById(nguoiDung.getId())) {
                throw new RuntimeException("ID người dùng đã tồn tại");
            }
            return nguoiDungRepository.save(nguoiDung);
        }

        // Generate ID mới nếu không có
        try {
            NguoiDung lastUser = nguoiDungRepository.findTopByOrderByIdDesc();
            if (lastUser != null) {
                try {
                    int lastNumber = Integer.parseInt(lastUser.getId());
                    String newId = String.format("%07d", lastNumber + 1); // ID có 7 ký tự
                    nguoiDung.setId(newId);
                } catch (NumberFormatException e) {
                    // Nếu ID không phải số, tạo UUID
                    String uniqueID = UUID.randomUUID().toString().replace("-", "").substring(0, 7);
                    nguoiDung.setId(uniqueID);
                }
            } else {
                nguoiDung.setId("0000001"); // ID ban đầu
            }
            return nguoiDungRepository.save(nguoiDung);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo ID người dùng mới", e);
        }
    }

    @Override
    public NguoiDung updateUser(String id, NguoiDung nguoiDung) {
        // Lấy trạng thái hiện tại của người dùng
        NguoiDung existingUser = nguoiDungRepository.findById(id).orElse(null);
        if (existingUser != null) {
            nguoiDung.setTrangThai(existingUser.getTrangThai()); // Giữ nguyên trạng thái
        }
        nguoiDung.setId(id);
        return nguoiDungRepository.save(nguoiDung);
    }

    @Override
    public void deleteUser(String id) {
        //  xóa( cập nhật trạng thái = 1 (true))
        NguoiDung nguoiDung = nguoiDungRepository.findById(id).orElse(null);
        if (nguoiDung != null) {
            nguoiDung.setTrangThai(true);
            nguoiDungRepository.save(nguoiDung);
        }
    }

    @Override
    public void toggleUserStatus(String id) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id).orElse(null);
        if (nguoiDung != null) {
            nguoiDung.setTrangThai(!nguoiDung.getTrangThai());
            nguoiDungRepository.save(nguoiDung);
        }
    }

    @Override
    public List<NguoiDung> searchUsers(String keyword) {
        return nguoiDungRepository.findByHoTenContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);
    }
}