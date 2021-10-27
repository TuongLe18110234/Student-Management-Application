package com.example.unionline.DTO;

import java.util.Date;

public class Semester {
    private String semesterId;
    private String semesterName;
    private Date startTime;
    private Date endTime;

    public Semester() {
    }

    public Semester(String semesterId, String semesterName, Date startTime, Date endTime) {
        this.semesterId = semesterId;
        this.semesterName = semesterName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}


