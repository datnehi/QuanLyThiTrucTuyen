package com.nhom6.server.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Exam {
    private String maKiThi;
    private String maMonHoc;
    private String tenKiThi;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoiGianTao;
    private int thoiGianThi;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoiGianBatDau;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoiGianKetThuc;
    private Boolean xemDiem;
    private Boolean xemDapAn;
    private Boolean hienThiBaiLam;
    private int soCau;
    private Boolean trangThai;

    public String getMaKiThi() {
        return maKiThi;
    }

    public void setMaKiThi(String maKiThi) {
        this.maKiThi = maKiThi;
    }

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public int getThoiGianThi() {
        return thoiGianThi;
    }

    public void setThoiGianThi(int thoiGianThi) {
        this.thoiGianThi = thoiGianThi;
    }

    public String getTenKiThi() {
        return tenKiThi;
    }

    public void setTenKiThi(String tenKiThi) {
        this.tenKiThi = tenKiThi;
    }

    public LocalDateTime getThoiGianTao() {
        return thoiGianTao;
    }

    public void setThoiGianTao(LocalDateTime thoiGianTao) {
        this.thoiGianTao = thoiGianTao;
    }

    public LocalDateTime getThoiGianBatDau() {
        return thoiGianBatDau;
    }

    public void setThoiGianBatDau(LocalDateTime thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }

    public Boolean getXemDiem() {
        return xemDiem;
    }

    public void setXemDiem(Boolean xemDiem) {
        this.xemDiem = xemDiem;
    }

    public LocalDateTime getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }

    public void setThoiGianKetThuc(LocalDateTime thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    public Boolean getXemDapAn() {
        return xemDapAn;
    }

    public void setXemDapAn(Boolean xemDapAn) {
        this.xemDapAn = xemDapAn;
    }

    public Boolean getHienThiBaiLam() {
        return hienThiBaiLam;
    }

    public void setHienThiBaiLam(Boolean hienThiBaiLam) {
        this.hienThiBaiLam = hienThiBaiLam;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }

    public int getSoCau() {
        return soCau;
    }

    public void setSoCau(int soCau) {
        this.soCau = soCau;
    }
}
