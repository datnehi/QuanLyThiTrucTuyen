package com.nhom6.server.Repository;

import com.nhom6.server.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByTrangThaiFalseOrderByThoiGianTaoDesc();

    Optional<Notification> findTopByOrderByMaThongBaoDesc();

    List<Notification> findByMaMonHocAndTrangThaiFalseOrderByThoiGianTaoDesc(String maMonHoc);

}
