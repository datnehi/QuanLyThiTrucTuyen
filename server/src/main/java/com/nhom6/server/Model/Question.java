package com.nhom6.server.Model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "cauhoi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @Column(length = 10)
    private String macauhoi;

    @Column(nullable = false, length = 255)
    private String noidung;

    @ManyToOne
    @JoinColumn(name = "mamonhoc", nullable = false)
    private Course monHoc;

    private boolean trangthai = false;

    @OneToMany(mappedBy = "cauHoi")
    private List<Answer> cauTraLoiList;

    @OneToMany(mappedBy = "cauHoi")
    private List<ChiTietBaiThi> chiTietDes;
}
