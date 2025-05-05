package com.nhom6.server.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chitiethongtbao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(DetailNotificationId.class)

public class DetailNotification {

    @Id
    @Column(name = "mathongbao", length = 10)
    private String maThongBao;

    @Id
    @Column(name = "mamonhoc", length = 5)
    private String maMonHoc;

    @Column(name = "trangthai")
    private boolean trangThai = false;
}
