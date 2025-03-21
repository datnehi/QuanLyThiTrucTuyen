package com.nhom6.server.Controller;


import com.nhom6.server.Model.Exam;
import com.nhom6.server.Model.Notification;
import com.nhom6.server.Services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
//lay du lieu
    @GetMapping
    public ResponseEntity<List<Notification>> getALLNotification(){
        List<Notification> notifications= notificationService.getALLNotification();
        return ResponseEntity.ok(notifications);
    }
    //tao thong bao
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification){
        Notification createNotification= notificationService.createNotification(notification);
        return ResponseEntity.ok(createNotification);
    }
    //cap nhat thong bao
    @PutMapping ("/{maThongBao}")
    public ResponseEntity<Notification> updateNotification(@PathVariable String maThongBao, @RequestBody Notification notificationDetails){
       try {
           Notification updateNotification = notificationService.updateNotification(maThongBao, notificationDetails);
           return ResponseEntity.ok(updateNotification);
       } catch (Exception e) {
           return ResponseEntity.notFound().build();
       }
    }
    //xoa thong bao
    @DeleteMapping ("/delete/{maThongBao}")
    public ResponseEntity<Notification> deleteNotification(@PathVariable String maThongBao){
        notificationService.deleteNotification(maThongBao);
        return ResponseEntity.noContent().build();
    }
    // Lấy kỳ thi theo mã
    @GetMapping("/{maThongBao}")
    public ResponseEntity<Notification> getExamById(@PathVariable String maThongBao) {
        try {
            Notification notification =notificationService.getNotificationById(maThongBao);
            return ResponseEntity.ok(notification);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
