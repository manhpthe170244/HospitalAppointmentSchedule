package com.example.project_prm392.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm392.api.ApiService;
import com.example.project_prm392.models.requests.LoginRequest;
import com.example.project_prm392.models.requests.RegisterRequest;
import com.example.project_prm392.models.responses.AuthResponse;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.LoginResponse;
import com.example.project_prm392.models.responses.UserResponse;
import com.example.project_prm392.utils.SessionManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class AuthRepository {
    private final ApiService apiService;
    private final SessionManager sessionManager;

    @Inject
    public AuthRepository(ApiService apiService, SessionManager sessionManager) {
        this.apiService = apiService;
        this.sessionManager = sessionManager;
    }

    public LiveData<BaseResponse<AuthResponse>> login(String email, String password) {
        MutableLiveData<BaseResponse<AuthResponse>> loginResult = new MutableLiveData<>();
        
        LoginRequest request = new LoginRequest(email, password);
        apiService.login(request).enqueue(new Callback<BaseResponse<AuthResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<AuthResponse>> call, Response<BaseResponse<AuthResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<AuthResponse> baseResponse = response.body();
                    if (baseResponse.isSuccess() && baseResponse.getData() != null) {
                        AuthResponse authResponse = baseResponse.getData();
                        sessionManager.saveAuthTokens(
                            authResponse.getAccessToken(),
                            authResponse.getRefreshToken()
                        );
                        sessionManager.saveUserInfo(
                            authResponse.getUserId(),
                            authResponse.getEmail(),
                            authResponse.getRole()
                        );
                    }
                    loginResult.setValue(baseResponse);
                } else {
                    loginResult.setValue(new BaseResponse<>(false, "Login failed", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<AuthResponse>> call, Throwable t) {
                loginResult.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });

        return loginResult;
    }

    public LiveData<BaseResponse<UserResponse>> register(RegisterRequest registerRequest) {
        MutableLiveData<BaseResponse<UserResponse>> registerResult = new MutableLiveData<>();
        
        apiService.register(registerRequest).enqueue(new Callback<BaseResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserResponse>> call, Response<BaseResponse<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    registerResult.setValue(response.body());
                } else {
                    registerResult.setValue(new BaseResponse<>(false, "Registration failed", null));
                }
            }
            
            @Override
            public void onFailure(Call<BaseResponse<UserResponse>> call, Throwable t) {
                registerResult.setValue(new BaseResponse<>(false, "Network error: " + t.getMessage(), null));
            }
        });
        
        return registerResult;
    }

    public LiveData<BaseResponse<AuthResponse>> refreshToken() {
        MutableLiveData<BaseResponse<AuthResponse>> refreshResult = new MutableLiveData<>();
        
        String refreshToken = sessionManager.getRefreshToken();
        if (refreshToken == null) {
            refreshResult.setValue(new BaseResponse<>(false, "No refresh token available", null));
            return refreshResult;
        }

        apiService.refreshToken(refreshToken).enqueue(new Callback<BaseResponse<AuthResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<AuthResponse>> call, Response<BaseResponse<AuthResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<AuthResponse> baseResponse = response.body();
                    if (baseResponse.isSuccess() && baseResponse.getData() != null) {
                        AuthResponse authResponse = baseResponse.getData();
                        sessionManager.saveAuthTokens(
                            authResponse.getAccessToken(),
                            authResponse.getRefreshToken()
                        );
                    }
                    refreshResult.setValue(baseResponse);
                } else {
                    refreshResult.setValue(new BaseResponse<>(false, "Token refresh failed", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<AuthResponse>> call, Throwable t) {
                refreshResult.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });

        return refreshResult;
    }

    public void logout() {
        sessionManager.clearSession();
    }

    public boolean isLoggedIn() {
        return sessionManager.isLoggedIn();
    }

    public String getUserRole() {
        return sessionManager.getUserRole();
    }
} 