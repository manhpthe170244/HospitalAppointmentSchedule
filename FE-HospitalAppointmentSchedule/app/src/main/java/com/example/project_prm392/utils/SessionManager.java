package com.example.project_prm392.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.project_prm392.MyApplication;
import com.example.project_prm392.model.User;
import com.google.gson.Gson;

public class SessionManager {
    private static SessionManager instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private SessionManager() {
        // Private constructor for singleton
    }

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
    }

    public void saveAuthToken(String token) {
        editor.putString(Constants.PREF_TOKEN, token);
        editor.apply();
    }

    public String getAuthToken() {
        return sharedPreferences.getString(Constants.PREF_TOKEN, null);
    }

    public void saveRefreshToken(String refreshToken) {
        editor.putString(Constants.PREF_REFRESH_TOKEN, refreshToken);
        editor.apply();
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(Constants.PREF_REFRESH_TOKEN, null);
    }

    public void saveUserId(int userId) {
        editor.putInt(Constants.PREF_USER_ID, userId);
        editor.apply();
    }

    public int getUserId() {
        return sharedPreferences.getInt(Constants.PREF_USER_ID, -1);
    }

    public void saveUsername(String username) {
        editor.putString(Constants.PREF_USERNAME, username);
        editor.apply();
    }

    public String getUsername() {
        return sharedPreferences.getString(Constants.PREF_USERNAME, "");
    }

    public boolean isLoggedIn() {
        return getAuthToken() != null;
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }

    public void saveUserRole(String role) {
        editor.putString(Constants.USER_ROLE, role);
        editor.apply();
    }

    public String getUserRole() {
        return sharedPreferences.getString(Constants.USER_ROLE, null);
    }

    public void saveUser(User user) {
        Gson gson = new Gson();
        String userJson = gson.toJson(user);
        editor.putString("user_data", userJson);
        editor.apply();

        saveUserId(user.getUserId());
        saveUserRole(user.getRole());
    }

    public User getUser() {
        String userJson = sharedPreferences.getString("user_data", null);
        if (userJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(userJson, User.class);
        }
        return null;
    }
} 