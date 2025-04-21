package com.nhom6.server.DTO;

public class SubmitExamRequest {
    private String maKiThi;
    private String id;
    private int timeUsed;

    @Override
    public String toString() {
        return "SubmitExamRequest{" +
                "maKiThi=" + maKiThi +
                ", id=" + id +
                ", timeUsed=" + timeUsed +
                '}';
    }

    public SubmitExamRequest(String maKiThi, String id, int timeUsed) {
        this.maKiThi = maKiThi;
        this.id = id;
        this.timeUsed = timeUsed;
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

    public int getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(int timeUsed) {
        this.timeUsed = timeUsed;
    }

}
