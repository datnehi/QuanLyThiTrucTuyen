package com.nhom6.server.Model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "nguoidung")
public class NguoiDung {

    @Id
    @Column(name = "id", length = 7)
    private String id;

    @Column(name = "hoten", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "gioitinh", nullable = false)
    private boolean gioiTinh;

    @Temporal(TemporalType.DATE)
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

    @Column(name = "trangthai")
    private boolean trangThai = false;

    // Constructors
    public NguoiDung() {
    }

    public NguoiDung(String id, String hoTen, boolean gioiTinh, Date ngaySinh, String soDienThoai,
                     String email, String matKhau, String role) {
        this.id = id;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.matKhau = matKhau;
        this.role = role;
    }

    // Getters and Setters
    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getHoTen() {return hoTen;}

    public void setHoTen(String hoTen) {this.hoTen = hoTen;}

    public boolean isGioiTinh() {return gioiTinh;}

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;}
    public Date getNgaySinh() {return ngaySinh;}

    public void setNgaySinh(Date ngaySinh) {this.ngaySinh = ngaySinh;}

    public String getAvatar() {return avatar;}

    public void setAvatar(String avatar) {this.avatar = avatar;}

    public String getSoDienThoai() {return soDienThoai;}

    public void setSoDienThoai(String soDienThoai) {this.soDienThoai = soDienThoai;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getMatKhau() {return matKhau;}

    public void setMatKhau(String matKhau) {this.matKhau = matKhau;}

    public String getRole() {return role;}

    public void setRole(String role) {this.role = role;}

    public boolean getTrangThai() {return trangThai;}

    public void setTrangThai(boolean trangThai) {this.trangThai = trangThai;}
    
}
