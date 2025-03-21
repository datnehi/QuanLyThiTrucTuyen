package com.nhom6.server.Services;

import com.nhom6.server.Model.Notification;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    // lấy thông tin bảng thông báo
    public List<Notification> getALLNotification(){
        String sql=" SELECT*FROM thongbao";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Notification.class));
    }
    // tạo thông báo mới
    public Notification createNotification(Notification notification) {
        String sql = "INSERT INTO thongbao (maThongBao, noiDung, thoiGianTao) VALUES (?, ?, ?)";
         jdbcTemplate.update(sql,
                notification.getMaThongBao(),
                notification.getNoiDung(),
                notification.getThoiGianTao()
        );
         return notification;
    }
    //cập nhật thông báo
    public Notification updateNotification(String maThongBao, Notification notificationDetails) {
        String sql = "UPDATE thongbao SET noiDung = ?, thoiGianTao = ? WHERE maThongBao = ?";
       int rowsAffected= jdbcTemplate.update(sql,
                notificationDetails.getNoiDung(),
                notificationDetails.getThoiGianTao(),
                maThongBao
        );
        if (rowsAffected == 0) {
            throw new RuntimeException("Không tìm thấy thông báo!");
        }

        return getNotificationById(maThongBao);
    }
    //xóa thông báo
    public void deleteNotification(String maThongBao){
        String sql="DELETE FROM thongbao WHERE maThongBao=? ";
        jdbcTemplate.update(sql,maThongBao);
    }
    //tim kiem thongbao theo id
    public Notification getNotificationById(String maThongBao){
        String sql = "SELECT * FROM thongbao WHERE maThongBao = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{maThongBao}, new BeanPropertyRowMapper<>(Notification.class));
    }

}
