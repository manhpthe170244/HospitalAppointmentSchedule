package com.example.project_prm392.model;

public class RefreshToken {
    private int refreshTokenId;
    private String token;
    private String expiresAt;
    private int userId;
    private User user;

    // Constructor
    public RefreshToken() {
    }

    public RefreshToken(int refreshTokenId, String token, String expiresAt, int userId) {
        this.refreshTokenId = refreshTokenId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.userId = userId;
    }

    // Getters and Setters
    public int getRefreshTokenId() {
        return refreshTokenId;
    }

    public void setRefreshTokenId(int refreshTokenId) {
        this.refreshTokenId = refreshTokenId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
} 