package com.nhom6.server.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cautraloi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

    @Id
    @Column(name = "macautraloi", length = 10)
    private String maCauTraLoi;

    @Column(name = "macauhoi", length = 10, nullable = false)
    private String maCauHoi;

    @Column(name = "noidung", nullable = false, length = 255)
    private String noiDung;

    @Column(name = "ladapan")
    private boolean laDapAn = false;
}
