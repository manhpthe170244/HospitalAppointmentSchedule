package com.example.project_prm392.utils;

public class Constants {
    // API Base URL
    public static final String BASE_URL = "https://your-api-base-url.com/api/";
    
    // API Endpoints
    public static final String ENDPOINT_LOGIN = "auth/login";
    public static final String ENDPOINT_REGISTER = "auth/register";
    public static final String ENDPOINT_REFRESH_TOKEN = "auth/refresh-token";
    public static final String ENDPOINT_DOCTORS = "doctors";
    public static final String ENDPOINT_SERVICES = "services";
    public static final String ENDPOINT_RESERVATIONS = "reservations";
    
    // Preferences
    public static final String PREFS_NAME = "hospital_prefs";
    public static final String PREF_TOKEN = "jwt_token";
    public static final String PREF_REFRESH_TOKEN = "refresh_token";
    public static final String PREF_USER_ID = "user_id";
    public static final String PREF_USERNAME = "username";
    public static final String USER_ROLE = "user_role";
    
    // Intent extras
    public static final String EXTRA_DOCTOR_ID = "extra_doctor_id";
    public static final String EXTRA_SERVICE_ID = "extra_service_id";
    public static final String EXTRA_RESERVATION_ID = "extra_reservation_id";
    
    // Request codes
    public static final int RC_SIGN_IN = 100;
    public static final int RC_GALLERY = 101;
    public static final int RC_CAMERA = 102;
} 