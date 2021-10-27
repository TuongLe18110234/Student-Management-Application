package com.example.unionline.DTO;

public class Parent_Student {
    private String id;
    private String parentId;
    private String studentId;

    public Parent_Student() {
    }

    public Parent_Student(String parentId, String studentId) {
        this.parentId = parentId;
        this.studentId = studentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
