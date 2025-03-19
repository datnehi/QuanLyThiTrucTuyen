package com.nhom6.server.Model;

import lombok.Data;

@Data
public class PhanMon {
    private String id;
    private String maMonHoc;
    private boolean trangThai;

    public PhanMon(String id, String maMonHoc) {
        this.id = id;
        this.maMonHoc = maMonHoc;
        this.trangThai = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}
