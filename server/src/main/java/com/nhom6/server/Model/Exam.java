package com.nhom6.server.Model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "kithi")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "makithi")
public class Exam {
    @Id
    @Column(length = 10)
    private String makithi;

    @ManyToOne
    @JoinColumn(name = "mamonhoc", nullable = false)
    private Course monHoc;

    @Column(nullable = false, length = 50)
    private String tenkithi;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoigiantao;

    @Column(nullable = false)
    private int thoigianthi;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoigianbatdau;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoigianketthuc;

    private boolean xemdiem = false;

    private boolean xemdapan = false;

    private boolean hienthibailam = false;

    @Column(nullable = false)
    private int socau;

    private boolean trangthai = false;

    @OneToMany(mappedBy = "kiThi")
    private List<Result> ketQuas;
}
