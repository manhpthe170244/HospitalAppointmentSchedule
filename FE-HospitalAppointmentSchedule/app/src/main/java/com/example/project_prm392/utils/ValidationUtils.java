package com.example.project_prm392.utils;

import android.util.Patterns;

public class ValidationUtils {
    public static boolean isValidEmail(String email) {
        return email != null && !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        // Password must be at least 6 characters long
        return password != null && password.length() >= 6;
    }

    public static boolean isValidPhone(String phone) {
        // Phone number must be at least 10 digits
        return phone != null && phone.matches("\\d{10,}");
    }
} 