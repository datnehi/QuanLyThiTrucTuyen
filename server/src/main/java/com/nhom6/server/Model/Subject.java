package com.nhom6.server.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "monhoc")
public class Subject {
    @Id
    @Column(name = "mamonhoc", length = 5)
    private String mamonhoc;
    
    @Column(name = "tenmonhoc", nullable = false, columnDefinition = "nvarchar(100)")
    private String tenmonhoc;
    
    @Column(name = "giangvien", columnDefinition = "nvarchar(100)")
    private String giangvien;
    
    @Column(name = "sotinchi")
    private Integer sotinchi;
    
    @Column(name = "ghichu", columnDefinition = "nvarchar(255)")
    private String ghichu;
    
    @Column(name = "trangthai")
    private Boolean trangthai;

    // Getters and Setters
    public String getMamonhoc() {
        return mamonhoc;
    }

    public void setMamonhoc(String mamonhoc) {
        this.mamonhoc = mamonhoc;
    }

    public String getTenmonhoc() {
        return tenmonhoc;
    }

    public void setTenmonhoc(String tenmonhoc) {
        this.tenmonhoc = tenmonhoc;
    }

    public String getGiangvien() {
        return giangvien;
    }

    public void setGiangvien(String giangvien) {
        this.giangvien = giangvien;
    }

    public Integer getSotinchi() {
        return sotinchi;
    }

    public void setSotinchi(Integer sotinchi) {
        this.sotinchi = sotinchi;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public Boolean getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(Boolean trangthai) {
        this.trangthai = trangthai;
    }
}