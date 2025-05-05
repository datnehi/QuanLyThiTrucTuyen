package com.nhom6.server.Model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ketqua")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    @Id
    @Column(name = "maketqua", length = 10)
    private String maKetQua;

    @Column(name = "makithi", length = 10, nullable = false)
    private String maKiThi;

    @Column(name = "id", length = 7, nullable = false)
    private String id;

    @Column(name = "diem")
    private Double diem;

    @Column(name = "thoigianvaothi")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoiGianVaoThi;

    @Column(name = "thoigianlambai")
    private Integer thoiGianLamBai;

    @Column(name = "socaudung")
    private Integer soCauDung;

}
