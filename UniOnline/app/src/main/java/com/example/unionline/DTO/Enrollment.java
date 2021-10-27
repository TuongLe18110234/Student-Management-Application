package com.example.unionline.DTO;

import java.io.Serializable;

public class Enrollment implements Serializable {
    private String id;
    private String classId;
    private String className;
    private String classRoom;
    private String fullDate;
    private String studentId;
    private String studentName;
    private String studentCode;
    private double midScore;
    private double finalScore;
    private int stateMark;
    private int classTuition;
    private boolean isPayClassTuition;
    public Enrollment(){};

    public Enrollment(String id, String classId, String className, String classRoom,
                      String fullDate, String studentId, String studentName, double midScore,
                      double finalScore, int stateMark, int classTuition, boolean isPayClassTuition) {
        this.id = id;
        this.classId = classId;
        this.className = className;
        this.classRoom = classRoom;
        this.fullDate = fullDate;
        this.studentId = studentId;
        this.studentName = studentName;
        this.midScore = midScore;
        this.finalScore = finalScore;
        this.stateMark = stateMark;
        this.classTuition = classTuition;
        this.isPayClassTuition = isPayClassTuition;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(String classRoom) {
        this.classRoom = classRoom;
    }

    public String getFullDate() {
        return fullDate;
    }

    public void setFullDate(String fullDate) {
        this.fullDate = fullDate;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public double getMidScore() {
        return midScore;
    }

    public void setMidScore(double midScore) {
        this.midScore = midScore;
    }

    public double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(double finalScore) {
        this.finalScore = finalScore;
    }

    public int getStateMark() {
        return stateMark;
    }

    public void setStateMark(int stateMark) {
        this.stateMark = stateMark;
    }

    public int getClassTuition() {
        return classTuition;
    }

    public void setClassTuition(int classTuition) {
        this.classTuition = classTuition;
    }

    public boolean isPayClassTuition() {
        return isPayClassTuition;
    }

    public void setPayClassTuition(boolean payClassTuition) {
        isPayClassTuition = payClassTuition;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }
}
