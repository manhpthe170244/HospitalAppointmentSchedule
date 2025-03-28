package com.example.project_prm392.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm392.api.ApiResponse;
import com.example.project_prm392.api.ServiceGenerator;
import com.example.project_prm392.api.request.LoginRequest;
import com.example.project_prm392.api.request.RefreshTokenRequest;
import com.example.project_prm392.api.request.RegisterRequest;
import com.example.project_prm392.api.response.LoginResponse;
import com.example.project_prm392.api.service.AuthService;
import com.example.project_prm392.utils.Resource;
import com.example.project_prm392.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final AuthService authService;
    private final SessionManager sessionManager;

    public AuthRepository() {
        this.authService = ServiceGenerator.getAuthService();
        this.sessionManager = SessionManager.getInstance();
    }

    public LiveData<Resource<LoginResponse>> login(String username, String password) {
        MutableLiveData<Resource<LoginResponse>> loginLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        loginLiveData.setValue(Resource.loading(null));
        
        // Gọi API đăng nhập
        LoginRequest loginRequest = new LoginRequest(username, password);
        authService.login(loginRequest).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<LoginResponse> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Lưu thông tin đăng nhập
                        LoginResponse loginResponse = apiResponse.getData();
                        sessionManager.saveAuthToken(loginResponse.getToken());
                        sessionManager.saveRefreshToken(loginResponse.getRefreshToken());
                        sessionManager.saveUser(loginResponse.getUser());
                        
                        // Thông báo thành công
                        loginLiveData.setValue(Resource.success(loginResponse));
                    } else {
                        // Thông báo lỗi từ server
                        loginLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    loginLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                // Thông báo lỗi mạng
                loginLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return loginLiveData;
    }

    public LiveData<Resource<LoginResponse>> register(String username, String password, String email, 
                                                     String fullName, String phoneNumber, String address) {
        MutableLiveData<Resource<LoginResponse>> registerLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        registerLiveData.setValue(Resource.loading(null));
        
        // Gọi API đăng ký
        RegisterRequest registerRequest = new RegisterRequest(
                username, password, email, fullName, phoneNumber, address, "Patient");
        
        authService.register(registerRequest).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<LoginResponse> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Lưu thông tin đăng nhập
                        LoginResponse loginResponse = apiResponse.getData();
                        sessionManager.saveAuthToken(loginResponse.getToken());
                        sessionManager.saveRefreshToken(loginResponse.getRefreshToken());
                        sessionManager.saveUser(loginResponse.getUser());
                        
                        // Thông báo thành công
                        registerLiveData.setValue(Resource.success(loginResponse));
                    } else {
                        // Thông báo lỗi từ server
                        registerLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    registerLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                // Thông báo lỗi mạng
                registerLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return registerLiveData;
    }

    public LiveData<Resource<LoginResponse>> refreshToken() {
        MutableLiveData<Resource<LoginResponse>> refreshTokenLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        refreshTokenLiveData.setValue(Resource.loading(null));
        
        // Lấy refresh token
        String refreshToken = sessionManager.getRefreshToken();
        if (refreshToken == null) {
            refreshTokenLiveData.setValue(Resource.error("Refresh token không tồn tại", null));
            return refreshTokenLiveData;
        }
        
        // Gọi API refresh token
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(refreshToken);
        
        authService.refreshToken(refreshTokenRequest).enqueue(new Callback<ApiResponse<LoginResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<LoginResponse> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Lưu thông tin đăng nhập mới
                        LoginResponse loginResponse = apiResponse.getData();
                        sessionManager.saveAuthToken(loginResponse.getToken());
                        sessionManager.saveRefreshToken(loginResponse.getRefreshToken());
                        
                        // Thông báo thành công
                        refreshTokenLiveData.setValue(Resource.success(loginResponse));
                    } else {
                        // Thông báo lỗi từ server
                        refreshTokenLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                        
                        // Xóa session nếu refresh token không hợp lệ
                        sessionManager.clearSession();
                    }
                } else {
                    // Thông báo lỗi HTTP
                    refreshTokenLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                    
                    // Xóa session nếu refresh token không hợp lệ
                    sessionManager.clearSession();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
                // Thông báo lỗi mạng
                refreshTokenLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return refreshTokenLiveData;
    }

    public void logout() {
        sessionManager.clearSession();
    }

    public boolean isLoggedIn() {
        return sessionManager.isLoggedIn();
    }
} 