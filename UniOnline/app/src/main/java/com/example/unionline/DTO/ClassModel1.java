package com.example.unionline.DTO;

import java.io.Serializable;

public class ClassModel1 implements Serializable {


    private String classId;
    private String semesterId;
    private String teacherId;
    private String className;
    private Integer capacity;
    private String room;
    private String startTime;
    private String endTime;
    private String startDate;
    private String endDate;
    private String state;
    private int tuition;
    private boolean isActive;

    public ClassModel1() {
    }

    public ClassModel1(String classId, String semesterId, String teacherId, String className, Integer capacity, String room, String startTime, String endTime, String startDate, String endDate, String state, boolean isActive) {
        this.classId = classId;
        this.semesterId = semesterId;
        this.teacherId = teacherId;
        this.className = className;
        this.capacity = capacity;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
        this.isActive = isActive;
    }

    public ClassModel1(String classId, String semesterId, String teacherId, String className, Integer capacity, String room, String startTime, String endTime, String state, boolean isActive) {
        this.classId = classId;
        this.semesterId = semesterId;
        this.teacherId = teacherId;
        this.className = className;
        this.capacity = capacity;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
        this.state = state;
        this.isActive = isActive;
    }

//    public ClassModel1(String classId, String semesterId, String teacherId, String className, Integer capacity, String room, String startTime, String endTime, boolean isActive) {
//        this.classId = classId;
//        this.semesterId = semesterId;
//        this.teacherId = teacherId;
//        this.className = className;
//        this.capacity = capacity;
//        this.room = room;
//        this.startTime = startTime;
//        this.endTime = endTime;
//        this.isActive = isActive;
//    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getTuition() {
        return tuition;
    }

    public void setTuition(int tuition) {
        this.tuition = tuition;
    }
}
