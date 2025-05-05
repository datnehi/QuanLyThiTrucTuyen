package com.nhom6.server.Model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "ketqua")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "maketqua")
public class Result {
    @Id
    @Column(length = 10)
    private String maketqua;

    @ManyToOne
    @JoinColumn(name = "makithi", nullable = false)
    private Exam kiThi;

    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User nguoiDung;

    private Double diem;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoigianvaothi;

    private Integer thoigianlambai;

    private Integer socaudung;

    @OneToMany(mappedBy = "ketQua")
    private List<ChiTietBaiThi> chiTietDes;
}