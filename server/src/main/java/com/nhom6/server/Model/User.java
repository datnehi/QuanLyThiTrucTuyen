package com.nhom6.server.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "nguoidung")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "id", length = 7)
    private String id;

    @Column(name = "hoten", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "gioitinh", nullable = false)
    private boolean gioiTinh;

    @Column(name = "ngaysinh", nullable = false)
    private Date ngaySinh;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "sodienthoai", nullable = false, length = 10)
    private String soDienThoai;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "matkhau", nullable = false, length = 50)
    private String matKhau;

    @Column(name = "role", nullable = false, length = 50)
    private String role;

    @Column(name = "trangthai", nullable = false)
    private boolean trangThai = false;

}

