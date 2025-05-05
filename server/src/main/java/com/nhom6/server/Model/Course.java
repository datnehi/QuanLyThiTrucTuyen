package com.nhom6.server.Model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "monhoc")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "mamonhoc")
public class Course {
    @Id
    @Column(length = 5)
    private String mamonhoc;

    @Column(nullable = false, length = 100)
    private String tenmonhoc;

    @Column(length = 100)
    private String giangvien;

    private Integer sotinchi;

    @Column(length = 255)
    private String ghichu;

    private boolean trangthai = false;

    @OneToMany(mappedBy = "monHoc")
    private List<Exam> kiThis;

    @OneToMany(mappedBy = "monHoc")
    private List<Question> cauHoiList;

    @OneToMany(mappedBy = "monHoc")
    private List<PhanMon> phanMons;
}