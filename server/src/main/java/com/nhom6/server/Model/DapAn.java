package com.nhom6.server.Model;

public class DapAn {
    private String maCauTraLoi;
    private String noiDungDapAn;
    private boolean daAnDung;
    private boolean dapAnChon;

    public DapAn(String maCauTraLoi, String noiDungDapAn, boolean daAnDung, boolean dapAnChon) {
        this.maCauTraLoi = maCauTraLoi;
        this.noiDungDapAn = noiDungDapAn;
        this.daAnDung = daAnDung;
        this.dapAnChon = dapAnChon;
    }

    public String getMaCauTraLoi() {
        return maCauTraLoi;
    }

    public void setMaCauTraLoi(String maCauTraLoi) {
        this.maCauTraLoi = maCauTraLoi;
    }

    public String getNoiDungDapAn() {
        return noiDungDapAn;
    }

    public void setNoiDungDapAn(String noiDungDapAn) {
        this.noiDungDapAn = noiDungDapAn;
    }

    public boolean isDaAnDung() {
        return daAnDung;
    }

    public void setDaAnDung(boolean daAnDung) {
        this.daAnDung = daAnDung;
    }

    public boolean isDapAnChon() {
        return dapAnChon;
    }

    public void setDapAnChon(boolean dapAnChon) {
        this.dapAnChon = dapAnChon;
    }
}
