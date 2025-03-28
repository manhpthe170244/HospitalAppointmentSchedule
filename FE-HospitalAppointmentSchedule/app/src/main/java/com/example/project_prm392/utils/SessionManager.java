package com.example.project_prm392.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "HospitalAppSession";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_ROLE = "userRole";
    private static final String KEY_USER_NAME = "userName";
    
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    private static SessionManager instance;

    private SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    public void saveAuthToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getAuthToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public void saveUserId(int userId) {
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    public void saveUserRole(String role) {
        editor.putString(KEY_USER_ROLE, role);
        editor.apply();
    }

    public String getUserRole() {
        return prefs.getString(KEY_USER_ROLE, null);
    }

    public void saveUserName(String userName) {
        editor.putString(KEY_USER_NAME, userName);
        editor.apply();
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, null);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }

    public boolean isLoggedIn() {
        return getAuthToken() != null;
    }
} 