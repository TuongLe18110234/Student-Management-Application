package com.example.unionline.DTO;

import android.text.format.DateFormat;

import java.io.Serializable;
import java.util.Calendar;

public class AbsenceApplication implements Serializable {
    private String id;
    private String classId;
    private String className;
    private String classTime;
    private String studentId;
    private String studentName;
    private String studentCode;
    private String reason;
    private String dateOff;
    private String dateCreate;
    private String teacherRespond;
    private int state;

    public AbsenceApplication(){}

    public AbsenceApplication(String id, String classId, String className, String classTime, String studentId, String studentName, String reason, String dateOff, int state) {
        this.id = id;
        this.classId = classId;
        this.className = className;
        this.classTime = classTime;
        this.studentId = studentId;
        this.studentName = studentName;
        this.reason = reason;
        this.dateOff = dateOff;
        this.dateCreate = DateFormat.format("hh:mm dd/MM/yyyy", Calendar.getInstance()).toString();;
        this.state = state;
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

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDateOff() {
        return dateOff;
    }

    public void setDateOff(String dateOff) {
        this.dateOff = dateOff;
    }

    public String getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public String getTeacherRespond() {
        return teacherRespond;
    }

    public void setTeacherRespond(String teacherRespond) {
        this.teacherRespond = teacherRespond;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }
}
