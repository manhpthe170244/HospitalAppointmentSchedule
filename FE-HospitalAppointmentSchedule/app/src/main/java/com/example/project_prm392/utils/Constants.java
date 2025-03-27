package com.example.project_prm392.utils;

public class Constants {
    // Base URL for API
    public static final String BASE_URL = "http://10.0.2.2:5000/api/"; // Android emulator localhost
    
    // Shared Preferences keys
    public static final String PREF_NAME = "hospital_app_prefs";
    public static final String KEY_AUTH_TOKEN = "auth_token";
    public static final String KEY_REFRESH_TOKEN = "refresh_token";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_ROLE = "user_role";
    
    // Response codes
    public static final int SUCCESS_CODE = 200;
    public static final int ERROR_CODE = 400;
    
    // Request codes
    public static final int REQUEST_PICK_IMAGE = 100;
    
    // Date formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DISPLAY_DATE_FORMAT = "MMMM dd, yyyy";
    public static final String DISPLAY_TIME_FORMAT = "hh:mm a";
} 