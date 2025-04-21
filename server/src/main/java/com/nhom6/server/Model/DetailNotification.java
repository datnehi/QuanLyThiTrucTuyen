package com.nhom6.server.Model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "chitiethongtbao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(DetailNotificationId.class)
public class DetailNotification {
    @Id
    @ManyToOne
    @JoinColumn(name = "mathongbao")
    private Notification thongBao;

    @Id
    @ManyToOne
    @JoinColumn(name = "mamonhoc")
    private Course monHoc;

    private boolean trangthai = false;
}
