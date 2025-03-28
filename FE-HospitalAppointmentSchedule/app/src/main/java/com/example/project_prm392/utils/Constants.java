package com.example.project_prm392.utils;

public class Constants {
    // API Base URL
    public static final String BASE_URL = "http://10.0.2.2:5158/api/"; // For Android Emulator
    // public static final String BASE_URL = "http://localhost:5158/api/"; // For local testing
    // public static final String BASE_URL = "https://10.0.2.2:7057/api/"; // For HTTPS on Android Emulator
    
    // SharedPreferences
    public static final String PREF_NAME = "HospitalAppPrefs";
    public static final String KEY_AUTH_TOKEN = "auth_token";
    public static final String KEY_REFRESH_TOKEN = "refresh_token";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_ROLE = "user_role";
    
    // Date Time Formats
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DISPLAY_DATE_FORMAT = "MMM dd, yyyy";
    public static final String DISPLAY_TIME_FORMAT = "hh:mm a";
    
    // API Endpoints
    public static final String LOGIN_URL = "auth/login";
    public static final String REGISTER_URL = "auth/register";
    public static final String REFRESH_TOKEN_URL = "auth/refresh-token";
    public static final String REVOKE_TOKEN_URL = "auth/revoke-token";
    
    // Doctor Endpoints
    public static final String GET_ALL_DOCTORS = "doctors";
    public static final String GET_DOCTOR_BY_ID = "doctors/{id}";
    public static final String GET_DOCTORS_BY_SPECIALTY = "doctors/specialty/{specialtyId}";
    
    // Specialty Endpoints
    public static final String GET_ALL_SPECIALTIES = "specialties";
    public static final String GET_SPECIALTY_BY_ID = "specialties/{id}";
    
    // Service Endpoints
    public static final String GET_ALL_SERVICES = "services";
    public static final String GET_SERVICE_BY_ID = "services/{id}";
    public static final String GET_SERVICES_BY_SPECIALTY = "services/specialty/{specialtyId}";
    
    // Appointment Endpoints
    public static final String GET_ALL_APPOINTMENTS = "appointments";
    public static final String GET_APPOINTMENT_BY_ID = "appointments/{id}";
    public static final String CREATE_APPOINTMENT = "appointments";
    public static final String UPDATE_APPOINTMENT = "appointments/{id}";
    public static final String CANCEL_APPOINTMENT = "appointments/{id}/cancel";
    
    // User Endpoints
    public static final String GET_ALL_USERS = "users";
    public static final String GET_USER_BY_ID = "users/{id}";
    public static final String UPDATE_USER = "users/{id}";
    
    // Statistics Endpoints
    public static final String GET_STATISTICS = "statistics";
    
    // Error Messages
    public static final String ERROR_NETWORK = "Network error occurred";
    public static final String ERROR_UNKNOWN = "An unknown error occurred";
    public static final String ERROR_SERVER = "Server error occurred";
    public static final String ERROR_UNAUTHORIZED = "Unauthorized access";
    public static final String ERROR_VALIDATION = "Validation error";
} 