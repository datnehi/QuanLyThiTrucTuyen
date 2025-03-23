package com.nhom6.server.DTO;

import java.util.Map;

public class SubmitExamRequest {
    private String maKiThi;
    private String id;
    private int timeUsed;
    private Map<String, String> answers; // Key: maCauHoi, Value: dapAnChon

    @Override
    public String toString() {
        return "SubmitExamRequest{" +
                "maKiThi=" + maKiThi +
                ", id=" + id +
                ", timeUsed=" + timeUsed +
                ", answers=" + answers +
                '}';
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

    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }
}
