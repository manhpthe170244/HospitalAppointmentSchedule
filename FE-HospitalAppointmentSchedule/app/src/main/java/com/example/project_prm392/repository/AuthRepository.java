package com.example.project_prm392.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm392.models.requests.LoginRequest;
import com.example.project_prm392.models.requests.RegisterRequest;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.LoginResponse;
import com.example.project_prm392.models.responses.UserResponse;
import com.example.project_prm392.network.ApiService;
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

    public LiveData<BaseResponse<LoginResponse>> login(String email, String password) {
        MutableLiveData<BaseResponse<LoginResponse>> loginResult = new MutableLiveData<>();
        
        LoginRequest loginRequest = new LoginRequest(email, password);
        
        apiService.login(loginRequest).enqueue(new Callback<BaseResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<LoginResponse>> call, Response<BaseResponse<LoginResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<LoginResponse> baseResponse = response.body();
                    
                    if (baseResponse.isSuccess() && baseResponse.getData() != null) {
                        // Save auth info
                        LoginResponse loginResponse = baseResponse.getData();
                        sessionManager.saveAuthToken(loginResponse.getToken());
                        sessionManager.saveRefreshToken(loginResponse.getRefreshToken());
                        sessionManager.saveUserId(loginResponse.getUserId());
                        sessionManager.saveUserName(loginResponse.getUserName());
                        sessionManager.saveUserEmail(loginResponse.getEmail());
                        
                        // Save user role (assuming the first role)
                        if (loginResponse.getRoles() != null && !loginResponse.getRoles().isEmpty()) {
                            sessionManager.saveUserRole(loginResponse.getRoles().get(0));
                        }
                    }
                    
                    loginResult.setValue(baseResponse);
                } else {
                    loginResult.setValue(new BaseResponse<>(false, "Login failed", null));
                }
            }
            
            @Override
            public void onFailure(Call<BaseResponse<LoginResponse>> call, Throwable t) {
                loginResult.setValue(new BaseResponse<>(false, "Network error: " + t.getMessage(), null));
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

    public LiveData<BaseResponse<LoginResponse>> refreshToken() {
        MutableLiveData<BaseResponse<LoginResponse>> refreshResult = new MutableLiveData<>();
        
        String refreshToken = sessionManager.getRefreshToken();
        if (refreshToken == null) {
            refreshResult.setValue(new BaseResponse<>(false, "No refresh token available", null));
            return refreshResult;
        }
        
        apiService.refreshToken(refreshToken).enqueue(new Callback<BaseResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<LoginResponse>> call, Response<BaseResponse<LoginResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<LoginResponse> baseResponse = response.body();
                    
                    if (baseResponse.isSuccess() && baseResponse.getData() != null) {
                        // Update tokens
                        LoginResponse loginResponse = baseResponse.getData();
                        sessionManager.saveAuthToken(loginResponse.getToken());
                        sessionManager.saveRefreshToken(loginResponse.getRefreshToken());
                    }
                    
                    refreshResult.setValue(baseResponse);
                } else {
                    refreshResult.setValue(new BaseResponse<>(false, "Token refresh failed", null));
                }
            }
            
            @Override
            public void onFailure(Call<BaseResponse<LoginResponse>> call, Throwable t) {
                refreshResult.setValue(new BaseResponse<>(false, "Network error: " + t.getMessage(), null));
            }
        });
        
        return refreshResult;
    }

    public void logout() {
        // Optionally call the revoke token API
        String refreshToken = sessionManager.getRefreshToken();
        if (refreshToken != null) {
            apiService.revokeToken(refreshToken).enqueue(new Callback<BaseResponse<Void>>() {
                @Override
                public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                    // Token revoked on server
                }
                
                @Override
                public void onFailure(Call<BaseResponse<Void>> call, Throwable t) {
                    // Failed to revoke token, but still clear locally
                }
            });
        }
        
        // Clear session data
        sessionManager.clearSession();
    }

    public boolean isLoggedIn() {
        return sessionManager.isLoggedIn();
    }

    public String getUserRole() {
        return sessionManager.getUserRole();
    }
} 