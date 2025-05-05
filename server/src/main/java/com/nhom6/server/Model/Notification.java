package com.nhom6.server.Model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "thongbao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @Column(name = "mathongbao", length = 10)
    private String maThongBao;

    @Column(name = "noidung", nullable = false, length = 255)
    private String noiDung;

    @Column(name = "thoigiantao", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoiGianTao;

    @Column(name = "trangthai")
    private boolean trangThai = false;
}
