package com.example.project_prm392.api.service;

import com.example.project_prm392.api.ApiResponse;
import com.example.project_prm392.api.request.LoginRequest;
import com.example.project_prm392.api.request.RefreshTokenRequest;
import com.example.project_prm392.api.request.RegisterRequest;
import com.example.project_prm392.api.response.LoginResponse;
import com.example.project_prm392.utils.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST(Constants.ENDPOINT_LOGIN)
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest loginRequest);

    @POST(Constants.ENDPOINT_REGISTER)
    Call<ApiResponse<LoginResponse>> register(@Body RegisterRequest registerRequest);

    @POST(Constants.ENDPOINT_REFRESH_TOKEN)
    Call<ApiResponse<LoginResponse>> refreshToken(@Body RefreshTokenRequest refreshTokenRequest);
} 