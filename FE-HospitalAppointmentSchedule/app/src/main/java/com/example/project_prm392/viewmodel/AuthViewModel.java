package com.example.project_prm392.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_prm392.api.response.LoginResponse;
import com.example.project_prm392.repository.AuthRepository;
import com.example.project_prm392.utils.Resource;

public class AuthViewModel extends ViewModel {
    private final AuthRepository authRepository;
    
    // Constructor cho Dependency Injection
    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
    
    public LiveData<Resource<LoginResponse>> login(String username, String password) {
        return authRepository.login(username, password);
    }
    
    public LiveData<Resource<LoginResponse>> register(String username, String password, String email, 
                                                     String fullName, String phoneNumber, String address) {
        return authRepository.register(username, password, email, fullName, phoneNumber, address);
    }
    
    public LiveData<Resource<LoginResponse>> refreshToken() {
        return authRepository.refreshToken();
    }
    
    public void logout() {
        authRepository.logout();
    }
    
    public boolean isLoggedIn() {
        return authRepository.isLoggedIn();
    }
} 