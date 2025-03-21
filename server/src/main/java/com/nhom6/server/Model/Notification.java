package com.nhom6.server.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notification {
    private String maThongBao;
    private String noiDung;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime thoiGianTao;

    public LocalDateTime getThoiGianTao() { return thoiGianTao;}

    public void setThoiGianTao(LocalDateTime thoiGianTao) {this.thoiGianTao = thoiGianTao;}

    public String getMaThongBao() {return maThongBao;}

    public void setMaThongBao(String maThongBao) {this.maThongBao = maThongBao;}

    public String getNoiDung() {return noiDung;}

    public void setNoiDung(String noiDung) {this.noiDung = noiDung;}
}
