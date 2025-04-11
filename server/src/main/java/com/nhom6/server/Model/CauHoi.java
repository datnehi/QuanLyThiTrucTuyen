package com.nhom6.server.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "cauhoi")
public class CauHoi {
    @Id
    @Column(name = "macauhoi", length = 10)
    private String macauhoi;
    
    @Column(name = "noidung", nullable = false, columnDefinition = "nvarchar(255)")
    private String noidung;
    
    @Column(name = "mamonhoc", length = 5, nullable = false)
    private String mamonhoc;
    
    @Column(name = "trangthai")
    private boolean trangthai = false;

    // Getters and Setters
    public String getMacauhoi() {
        return macauhoi;
    }

    public void setMacauhoi(String macauhoi) {
        this.macauhoi = macauhoi;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getMamonhoc() {
        return mamonhoc;
    }

    public void setMamonhoc(String mamonhoc) {
        this.mamonhoc = mamonhoc;
    }

    public boolean isTrangthai() {
        return trangthai;
    }

    public void setTrangthai(boolean trangthai) {
        this.trangthai = trangthai;
    }
}