package com.nhom6.server.Model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "nguoidung")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
    @Id
    @Column(length = 7)
    private String id;

    @Column(nullable = false, length = 100)
    private String hoten;

    @Column(nullable = false)
    private boolean gioitinh;

    @Column(nullable = false)
    private Date ngaysinh;

    @Column(length = 255)
    private String avatar;

    @Column(nullable = false, length = 10)
    private String sodienthoai;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String matkhau;

    @Column(nullable = false, length = 50)
    private String role;

    private boolean trangthai = false;

    @OneToMany(mappedBy = "nguoiDung")
    private List<PhanMon> phanMons;

    @OneToMany(mappedBy = "nguoiDung")
    private List<Result> ketQuas;
}
