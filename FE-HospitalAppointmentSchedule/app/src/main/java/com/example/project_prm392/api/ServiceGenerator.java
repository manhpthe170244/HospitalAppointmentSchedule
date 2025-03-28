package com.example.project_prm392.api;

import com.example.project_prm392.api.service.AuthService;
import com.example.project_prm392.api.service.DoctorService;
import com.example.project_prm392.api.service.ReservationService;
import com.example.project_prm392.api.service.ServiceApiService;
import com.example.project_prm392.api.service.UserService;

public class ServiceGenerator {
    private static AuthService authService;
    private static UserService userService;
    private static DoctorService doctorService;
    private static ReservationService reservationService;
    private static ServiceApiService serviceApiService;

    public static AuthService getAuthService() {
        if (authService == null) {
            authService = ApiClient.getClient().create(AuthService.class);
        }
        return authService;
    }

    public static UserService getUserService() {
        if (userService == null) {
            userService = ApiClient.getClient().create(UserService.class);
        }
        return userService;
    }

    public static DoctorService getDoctorService() {
        if (doctorService == null) {
            doctorService = ApiClient.getClient().create(DoctorService.class);
        }
        return doctorService;
    }

    public static ReservationService getReservationService() {
        if (reservationService == null) {
            reservationService = ApiClient.getClient().create(ReservationService.class);
        }
        return reservationService;
    }

    public static ServiceApiService getServiceApiService() {
        if (serviceApiService == null) {
            serviceApiService = ApiClient.getClient().create(ServiceApiService.class);
        }
        return serviceApiService;
    }
} 