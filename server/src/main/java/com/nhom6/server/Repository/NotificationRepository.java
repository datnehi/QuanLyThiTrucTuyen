package com.nhom6.server.Repository;

import com.nhom6.server.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByTrangThaiFalseOrderByThoiGianTaoDesc();

    Optional<Notification> findTopByOrderByMaThongBaoDesc();

    List<Notification> findByMaMonHocAndTrangThaiFalseOrderByThoiGianTaoDesc(String maMonHoc);

    @Query("SELECT n FROM Notification n " +
            "JOIN PhanMon p ON n.maMonHoc = p.maMonHoc " +
            "WHERE p.id = :studentId")
    List<Notification> findNotificationsByStudentId(@Param("studentId") String studentId);
}
