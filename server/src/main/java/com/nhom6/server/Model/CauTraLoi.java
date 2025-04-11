package com.nhom6.server.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "cautraloi")
public class CauTraLoi {
    @Id
    @Column(name = "macautraloi", length = 10)
    private String macautraloi;
    
    @Column(name = "macauhoi", length = 10, nullable = false)
    private String macauhoi;
    
    @Column(name = "noidung", nullable = false, columnDefinition = "nvarchar(255)")
    private String noidung;
    
    @Column(name = "ladapan")
    private boolean ladapan = false;

    // Getters and Setters
    public String getMacautraloi() {
        return macautraloi;
    }

    public void setMacautraloi(String macautraloi) {
        this.macautraloi = macautraloi;
    }

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

    public boolean isLadapan() {
        return ladapan;
    }

    public void setLadapan(boolean ladapan) {
        this.ladapan = ladapan;
    }
}