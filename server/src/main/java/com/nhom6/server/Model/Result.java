package com.nhom6.server.Model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Result {
    private String maKetQua;
    private String maKiThi;
    private String id;
    private Float diem;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoiGianVaoThi;
    private Float thoiGianLamBai;
    private Integer soCauDung;

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

    public Float getDiem() {
        return diem;
    }

    public void setDiem(Float diem) {
        this.diem = diem;
    }

    public LocalDateTime getThoiGianVaoThi() {
        return thoiGianVaoThi;
    }

    public void setThoiGianVaoThi(LocalDateTime thoiGianVaoThi) {
        this.thoiGianVaoThi = thoiGianVaoThi;
    }

    public Float getThoiGianLamBai() {
        return thoiGianLamBai;
    }

    public void setThoiGianLamBai(Float thoiGianLamBai) {
        this.thoiGianLamBai = thoiGianLamBai;
    }

    public Integer getSoCauDung() {
        return soCauDung;
    }

    public void setSoCauDung(Integer soCauDung) {
        this.soCauDung = soCauDung;
    }
}
