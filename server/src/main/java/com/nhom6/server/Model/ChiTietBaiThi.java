package com.nhom6.server.Model;

import java.util.ArrayList;
import java.util.List;

public class ChiTietBaiThi {
    private String maCauHoi;
    private String noiDungCauHoi;
    private List<DapAn> dapAnList;
    private int thuTu;

    public ChiTietBaiThi(String maCauHoi, String noiDungCauHoi, int thuTu) {
        this.maCauHoi = maCauHoi;
        this.noiDungCauHoi = noiDungCauHoi;
        this.dapAnList = new ArrayList<>();
        this.thuTu = thuTu;
    }

    public ChiTietBaiThi(String maCauHoi, String noiDungCauHoi, List<DapAn> dapAnList, int thuTu) {
        this.maCauHoi = maCauHoi;
        this.noiDungCauHoi = noiDungCauHoi;
        this.dapAnList = dapAnList;
        this.thuTu = thuTu;
    }

    public void addDapAn(DapAn dapan) {
        this.dapAnList.add(dapan);
    }

    public String getMaCauHoi() {
        return maCauHoi;
    }

    public void setMaCauHoi(String maCauHoi) {
        this.maCauHoi = maCauHoi;
    }

    public String getNoiDungCauHoi() {
        return noiDungCauHoi;
    }

    public void setNoiDungCauHoi(String noiDungCauHoi) {
        this.noiDungCauHoi = noiDungCauHoi;
    }

    public List<DapAn> getDapAnList() {
        return dapAnList;
    }

    public void setDapAnList(List<DapAn> dapAnList) {
        this.dapAnList = dapAnList;
    }

    public int getThuTu() {
        return thuTu;
    }

    public void setThuTu(int thuTu) {
        this.thuTu = thuTu;
    }
}
