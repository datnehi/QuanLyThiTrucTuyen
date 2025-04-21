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
    @Column(length = 10)
    private String macautraloi;

    @ManyToOne
    @JoinColumn(name = "macauhoi", nullable = false)
    private Question cauHoi;

    @Column(nullable = false, length = 255)
    private String noidung;

    private boolean ladapan = false;
}
