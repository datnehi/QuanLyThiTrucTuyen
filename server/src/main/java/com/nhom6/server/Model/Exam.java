package com.nhom6.server.Model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "kithi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exam {

    @Id
    @Column(name = "makithi", length = 10)
    private String maKiThi;

    @Column(name = "mamonhoc", length = 5, nullable = false)
    private String maMonHoc;

    @Column(name = "tenkithi", length = 50, nullable = false)
    private String tenKiThi;

    @Column(name = "thoigiantao", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoiGianTao;

    @Column(name = "thoigianthi", nullable = false)
    private int thoiGianThi;

    @Column(name = "thoigianbatdau", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoiGianBatDau;

    @Column(name = "thoigianketthuc", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoiGianKetThuc;

    @Column(name = "xemdiem")
    private boolean xemDiem = false;

    @Column(name = "xemdapan")
    private boolean xemDapAn = false;

    @Column(name = "hienthibailam")
    private boolean hienThiBaiLam = false;

    @Column(name = "socau", nullable = false)
    private int soCau;

    @Column(name = "trangthai")
    private boolean trangThai = false;
}
