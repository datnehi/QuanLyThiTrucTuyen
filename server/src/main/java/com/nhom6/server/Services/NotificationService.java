package com.nhom6.server.Services;

import com.nhom6.server.Model.Notification;
import com.nhom6.server.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class NotificationService {

@Autowired
private NotificationRepository notificationRepository;

    // Lấy tất cả thông báo
    public List<Notification> getAll() {
        try {
            return notificationRepository.findByTrangThaiFalseOrderByThoiGianTaoDesc();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    // Lấy thông báo theo maMonHoc
    public List<Notification> getNotificationsByMaMonHoc(String maMonHoc) {
        try {
            return notificationRepository.findByMaMonHocAndTrangThaiFalseOrderByThoiGianTaoDesc(maMonHoc);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Notification> getNotificationsByStudentId(String studentId) {
        try {
            return notificationRepository.findNotificationsByStudentId(studentId);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    // Tạo mã thông báo tiếp theo
    private String generateNextMaThongBao() {
        Optional<Notification> lastNotification = notificationRepository.findTopByOrderByMaThongBaoDesc();
        if (lastNotification.isPresent()) {
            String lastMaKiThi = lastNotification.get().getMaThongBao();
            long number = Long.parseLong(lastMaKiThi);
            return String.format("%010d", ++number);
        }
        return "0000000001";
    }

    // Tạo mới hoặc cập nhật thông báo
    public Optional<Notification> createNotification(Notification notification) {
        try {
            String maThongBao = generateNextMaThongBao();
            notification.setMaThongBao(maThongBao);

            return Optional.of(notificationRepository.save(notification));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    // Tìm thông báo theo ID
    public Optional<Notification> getNotificationByMaThongBao(String maThongBao) {
        try {
            return notificationRepository.findById(maThongBao);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    //cap nhat thong bao
    public Optional<Notification> updateNotification(String maThongBao, Notification notificationDetails) {
        try {
            Optional<Notification> exitstingNotification = getNotificationByMaThongBao(maThongBao);
            if (exitstingNotification.isPresent()) {
                Notification notification = exitstingNotification.get();
                notification.setNoiDung(notificationDetails.getNoiDung());

                return Optional.of(notificationRepository.save(notification));
            }
            return Optional.empty();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean deleteNotification(String maThongBao) {
        try {
            Optional<Notification> notification = notificationRepository.findById(maThongBao);
            if (notification.isPresent()) {
                notification.get().setTrangThai(true);
                notificationRepository.save(notification.get());
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
