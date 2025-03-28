package com.example.project_prm392.api.response;

import com.example.project_prm392.model.User;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("refreshToken")
    private String refreshToken;

    @SerializedName("user")
    private User user;

    public LoginResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
} 