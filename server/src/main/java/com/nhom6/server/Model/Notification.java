package com.nhom6.server.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "thongbao")
@Data
public class Notification {

    @Id
    @Column(name = "mathongbao", length = 10)
    private String maThongBao;

    @Column(name = "noidung", nullable = false, length = 255)
    private String noiDung;

    @Column(name = "thoigiantao", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoiGianTao;

    @Column(name = "trangthai")
    private Boolean trangThai;


    public Notification() {
    }

    // Optional: All-args constructor
    public Notification(String maThongBao, String noiDung, LocalDateTime thoiGianTao, Boolean trangThai) {
        this.maThongBao = maThongBao;
        this.noiDung = noiDung;
        this.thoiGianTao = thoiGianTao;
        this.trangThai = trangThai;
    }

    public LocalDateTime getThoiGianTao() {return thoiGianTao;}

    public void setThoiGianTao(LocalDateTime thoiGianTao) {this.thoiGianTao = thoiGianTao;}

    public String getMaThongBao() {return maThongBao;}

    public void setMaThongBao(String maThongBao) {this.maThongBao = maThongBao;}

    public String getNoiDung() {return noiDung;}

    public void setNoiDung(String noiDung) {this.noiDung = noiDung;}

    public Boolean getTrangThai() {return trangThai;}

    public void setTrangThai(Boolean trangThai) {this.trangThai = trangThai;}
}