package com.example.project_prm392.api.service;

import com.example.project_prm392.api.ApiResponse;
import com.example.project_prm392.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @GET("users/{id}")
    Call<ApiResponse<User>> getUserById(@Path("id") int userId);

    @GET("users/profile")
    Call<ApiResponse<User>> getCurrentUserProfile();

    @PUT("users/{id}")
    Call<ApiResponse<User>> updateUser(@Path("id") int userId, @Body User user);

    @POST("users/change-password")
    Call<ApiResponse<Boolean>> changePassword(@Body PasswordChangeRequest request);
} 