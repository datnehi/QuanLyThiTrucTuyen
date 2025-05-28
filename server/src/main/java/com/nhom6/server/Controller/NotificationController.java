package com.nhom6.server.Controller;

import com.nhom6.server.Model.Notification;
import com.nhom6.server.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Lấy tất cả thông báo
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllNotifications() {
        try {
            List<Notification> notifications = notificationService.getAll();
            return ResponseEntity.ok(Map.of(
                    "message", "Lấy danh sách thông báo thành công",
                    "data", notifications
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Lỗi khi lấy danh sách thông báo"
            ));
        }
    }

    // Lấy thông báo theo mã
    @GetMapping("/{maThongBao}")
    public ResponseEntity<Map<String, Object>> getNotificationById(@PathVariable String maThongBao) {
        try {
            Optional<Notification> notification = notificationService.getNotificationByMaThongBao(maThongBao);
            return ResponseEntity.ok(Map.of(
                    "message", "Lấy thông báo thành công",
                    "data", notification
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy thông báo với mã: " + maThongBao
            ));
        }
    }

    //Lấy thông báo theo mã môn học
    @GetMapping("/mamonhoc/{maMonHoc}")
    public ResponseEntity<Map<String, Object>> getExamByMaMonHoc(@PathVariable String maMonHoc) {
        try {
            List<Notification> notifications = notificationService.getNotificationsByMaMonHoc(maMonHoc);
            return ResponseEntity.ok(Map.of(
                    "message", "Tìm kiếm thông báo theo môn học thành công",
                    "data", notifications
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Lỗi khi tìm kiếm thông báo theo môn học"
            ));
        }
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<Map<String, Object>> getNotifications(@PathVariable("id") String studentId) {
        try {
            List<Notification> notifications = notificationService.getNotificationsByStudentId(studentId);
            return ResponseEntity.ok(Map.of(
                    "message", "Tìm kiếm thông báo theo môn học thành công",
                    "data", notifications
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "message", "Lỗi khi tìm kiếm thông báo theo môn học"
            ));
        }
    }

    // Tạo thông báo mới
    @PostMapping
    public ResponseEntity<Map<String, Object>> createNotification(@RequestBody Notification notification) {
        if (notification.getNoiDung() == null || notification.getNoiDung().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Nội dung thông báo không được để trống"));
        }

        if (notification.getMaMonHoc() == null || notification.getMaMonHoc().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Mã môn học không được để trống"));
        }
        try {
            Optional<Notification> createdNotification = notificationService.createNotification(notification);
            return ResponseEntity.status(201).body(Map.of(
                    "message", "Tạo thông báo thành công",
                    "data", createdNotification
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                    "message", "Lỗi khi tạo thông báo"
            ));
        }
    }

    // Cập nhật thông báo
    @PutMapping("/{maThongBao}")
    public ResponseEntity<Map<String, Object>> updateNotification(
            @PathVariable String maThongBao,
            @RequestBody Notification thongBaoDetails) {
        if (thongBaoDetails.getNoiDung() == null || thongBaoDetails.getNoiDung().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Nội dung thông báo không được để trống"));
        }
        try {
            Optional<Notification> updatedNotification = notificationService.updateNotification(maThongBao, thongBaoDetails);
            return ResponseEntity.ok(Map.of(
                    "message", "Cập nhật thông báo thành công",
                    "data", updatedNotification
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "message", "Không tìm thấy thông báo để cập nhật"
            ));
        }
    }

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
