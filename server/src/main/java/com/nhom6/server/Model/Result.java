package com.nhom6.server.Model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Result {
    private String maKetQua;
    private String maKiThi;
    private String id;
    private float diem;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoiGianVaoThi;
    private float thoiGianLamBai;
    private int soCauDung;

    public String getMaKetQua() {
        return maKetQua;
    }

    public void setMaKetQua(String maKetQua) {
        this.maKetQua = maKetQua;
    }

    public String getMaKiThi() {
        return maKiThi;
    }

    public void setMaKiThi(String maKiThi) {
        this.maKiThi = maKiThi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getDiem() {
        return diem;
    }

    public void setDiem(float diem) {
        this.diem = diem;
    }

    public LocalDateTime getThoiGianVaoThi() {
        return thoiGianVaoThi;
    }

    public void setThoiGianVaoThi(LocalDateTime thoiGianVaoThi) {
        this.thoiGianVaoThi = thoiGianVaoThi;
    }

    public float getThoiGianLamBai() {
        return thoiGianLamBai;
    }

    public void setThoiGianLamBai(float thoiGianLamBai) {
        this.thoiGianLamBai = thoiGianLamBai;
    }

    public int getSoCauDung() {
        return soCauDung;
    }

    public void setSoCauDung(int soCauDung) {
        this.soCauDung = soCauDung;
    }
}
