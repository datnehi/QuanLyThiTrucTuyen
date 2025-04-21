package com.nhom6.server.Controller;

import com.nhom6.server.Model.Notification;
import com.nhom6.server.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Lấy tất cả thông báo
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        List<Notification> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    // Lấy thông báo theo mã
    @GetMapping("/{maThongBao}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable String maThongBao) {
        Notification notification = notificationService.getNotificationById(maThongBao);
        return ResponseEntity.ok(notification);
    }

    // Tạo thông báo mới
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification createdNotification = notificationService.createNotification(notification);
        return ResponseEntity.ok(createdNotification);
    }

    // Cập nhật thông báo
    @PutMapping("/{maThongBao}")
    public ResponseEntity<Notification> updateNotification(
            @PathVariable String maThongBao,
            @RequestBody Notification thongBaoDetails) {
        Notification updatedNotification = notificationService.updateNotification(maThongBao, thongBaoDetails);
        return ResponseEntity.ok(updatedNotification);
    }
   // xóa thong báo( đặt từ 0->1)
    @DeleteMapping("/{maThongBao}")
    public ResponseEntity<Map<String, Object>> deleteNotification(@PathVariable String maThongBao) {
        try {
            notificationService.deleteNotification(maThongBao);
            return ResponseEntity.ok(Map.of(
                    "message", "Xóa thông báo thành công."
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Không tìm thấy thông báo với mã: " + maThongBao
            ));
        }
    }
}
