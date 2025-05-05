package com.nhom6.server.Repository;

import com.nhom6.server.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    Notification findTopByOrderByMaThongBaoDesc();
}
