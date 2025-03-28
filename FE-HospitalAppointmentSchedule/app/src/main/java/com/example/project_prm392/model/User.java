package com.example.project_prm392.model;

import java.util.List;

public class User {
    private int userId;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String avatar;
    private String role;
    private String image;
    private List<RefreshToken> refreshTokens;

    // Constructor
    public User() {
    }

    public User(int userId, String username, String email, String password, String fullName, 
                String phoneNumber, String address, String avatar, String role, String image) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.avatar = avatar;
        this.role = role;
        this.image = image;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<RefreshToken> getRefreshTokens() {
        return refreshTokens;
    }

    public void setRefreshTokens(List<RefreshToken> refreshTokens) {
        this.refreshTokens = refreshTokens;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
} 