package com.nhom6.server.Services;

import com.nhom6.server.Model.Notification;
import com.nhom6.server.Repon.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // Lấy tất cả thông báo

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    // Tạo mới hoặc cập nhật thông báo
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }
    // Tìm thông báo theo ID
    public Notification getNotificationById(String maThongBao) {
        return notificationRepository.findById(maThongBao).orElseThrow(() -> new RuntimeException("Thông báo không tồn tại với mã: " + maThongBao));
    }
    //cap nhat thong bao
    public Notification updateNotification(String maThongBao, Notification thongBaoDetails) {
        Notification thongBao = getNotificationById(maThongBao);

        thongBao.setNoiDung(thongBaoDetails.getNoiDung());
        thongBao.setTrangThai(thongBaoDetails.getTrangThai());
        // Don't update thoiGianTao when updating

        return notificationRepository.save(thongBao);
    }
    public Notification deleteNotification(String maThongBao) {
        Notification notification = notificationRepository.findById(maThongBao)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông báo"));

        notification.setTrangThai(true);
        return notificationRepository.save(notification);
    }

}
