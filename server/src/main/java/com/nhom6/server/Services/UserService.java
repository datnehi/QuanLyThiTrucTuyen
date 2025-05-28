package com.nhom6.server.Services;

import com.nhom6.server.Model.User;
import com.nhom6.server.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> login(String id, String matkhau) {
        return userRepository.findByIdAndMatKhauAndTrangThaiFalse(id, matkhau);
    }

    public List<User> getAll() {

        return userRepository.findAll();
    }

    public List<User> getActiveUsers() {
        return userRepository.findByTrangThai(false); // Trạng thái = 0 (false) - active
    }

    public User createUser(User nguoiDung) {
        // Mặc định trạng thái là 0 (false) khi tạo mới
        nguoiDung.setTrangThai(false);

        // Nếu đã có ID, kiểm tra xem có tồn tại không
        if (nguoiDung.getId() != null && !nguoiDung.getId().isEmpty()) {
            if (userRepository.existsById(nguoiDung.getId())) {
                throw new RuntimeException("ID người dùng đã tồn tại");
            }
            return userRepository.save(nguoiDung);
        }

        // Generate ID mới nếu không có
        try {
            User lastUser = userRepository.findTopByOrderByIdDesc();
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
            return userRepository.save(nguoiDung);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo ID người dùng mới", e);
        }
    }

    public User updateUser(String id, User nguoiDung) {
        // Lấy trạng thái hiện tại của người dùng
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            nguoiDung.setTrangThai(nguoiDung.isTrangThai()); // Giữ nguyên trạng thái
        }
        nguoiDung.setId(id);
        return userRepository.save(nguoiDung);
    }

    public void deleteUser(String id) {
        //  xóa( cập nhật trạng thái = 1 (true))
        User nguoiDung = userRepository.findById(id).orElse(null);
        if (nguoiDung != null) {
            nguoiDung.setTrangThai(true);
            userRepository.save(nguoiDung);
        }
    }

    public List<User> searchUsers(String keyword) {
        return userRepository.findByHoTenContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);
    }
    // Đổi mật khẩu 
    @Transactional
    public User updateUserPassword(String id, String currentPassword, String newPassword) {
        // Validate input
        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu hiện tại không được để trống");
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu mới không được để trống");
        }

        if (newPassword.length() > 50) {
            throw new IllegalArgumentException("Mật khẩu mới không được vượt quá 50 ký tự");
        }

        if (currentPassword.equals(newPassword)) {
            throw new IllegalArgumentException("Mật khẩu mới phải khác mật khẩu hiện tại");
        }

        // Find user
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + id));

        // Kiểm tra mật khẩu hiện tại (so sánh trực tiếp)
        if (!currentPassword.equals(user.getMatKhau())) {
            throw new RuntimeException("Mật khẩu hiện tại không chính xác");
        }

        // Cập nhật mật khẩu mới (
        user.setMatKhau(newPassword);

        return userRepository.save(user);
    }
}