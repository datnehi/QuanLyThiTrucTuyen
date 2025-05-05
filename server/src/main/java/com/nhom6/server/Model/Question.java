package com.nhom6.server.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cauhoi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @Column(name = "macauhoi", length = 10)
    private String maCauHoi;

    @Column(name = "noidung", nullable = false, length = 255)
    private String noiDung;

    @Column(name = "mamonhoc", length = 10, nullable = false)
    private String maMonHoc;

    @Column(name = "trangthai")
    private boolean trangThai = false;
}
