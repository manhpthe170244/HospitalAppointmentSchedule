package com.example.project_prm392.utils;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class SessionManager {
    private final SharedPreferences sharedPreferences;
    
    @Inject
    public SessionManager(@ApplicationContext Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }
    
    public void saveAuthToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_AUTH_TOKEN, token);
        editor.apply();
    }
    
    public String getAuthToken() {
        return sharedPreferences.getString(Constants.KEY_AUTH_TOKEN, null);
    }
    
    public void saveRefreshToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_REFRESH_TOKEN, token);
        editor.apply();
    }
    
    public String getRefreshToken() {
        return sharedPreferences.getString(Constants.KEY_REFRESH_TOKEN, null);
    }
    
    public void saveUserId(int userId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constants.KEY_USER_ID, userId);
        editor.apply();
    }
    
    public int getUserId() {
        return sharedPreferences.getInt(Constants.KEY_USER_ID, -1);
    }
    
    public void saveUserName(String userName) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_USER_NAME, userName);
        editor.apply();
    }
    
    public String getUserName() {
        return sharedPreferences.getString(Constants.KEY_USER_NAME, null);
    }
    
    public void saveUserEmail(String userEmail) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_USER_EMAIL, userEmail);
        editor.apply();
    }
    
    public String getUserEmail() {
        return sharedPreferences.getString(Constants.KEY_USER_EMAIL, null);
    }
    
    public void saveUserRole(String userRole) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_USER_ROLE, userRole);
        editor.apply();
    }
    
    public String getUserRole() {
        return sharedPreferences.getString(Constants.KEY_USER_ROLE, null);
    }
    
    public void clearSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
    
    public boolean isLoggedIn() {
        return getAuthToken() != null;
    }
} 