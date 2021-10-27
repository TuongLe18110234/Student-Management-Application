package com.example.unionline.DTO;

public class User {
    private String userId;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String major;
    private String address;
    private boolean isActive;
    private int role;
    private String gender;

    public User(String userId, String name, String email, String phone, String major, boolean isActive, int role, String address, String gender) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.isActive = isActive;
        this.major = major;
        this.role = role;
        this.address = address;
        this.gender = gender;
    }

    public User() {

    }

    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMajor() { return major; }

    public void setMajor(String major) { this.major = major; }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) { isActive = active; }

    public int getRole() { return role; }

    public void setRole(int role) { this.role = role; }
}
