package com.example.project_prm392.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_prm392.models.responses.AuthResponse;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.repository.AuthRepository;
import com.example.project_prm392.utils.SessionManager;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final SessionManager sessionManager;

    @Inject
    public AuthViewModel(AuthRepository authRepository, SessionManager sessionManager) {
        this.authRepository = authRepository;
        this.sessionManager = sessionManager;
    }

    public LiveData<BaseResponse<AuthResponse>> login(String email, String password) {
        return authRepository.login(email, password);
    }

    public LiveData<BaseResponse<AuthResponse>> refreshToken() {
        return authRepository.refreshToken();
    }

    public void logout() {
        authRepository.logout();
    }

    public boolean isLoggedIn() {
        return sessionManager.isLoggedIn();
    }

    public boolean isAdmin() {
        return sessionManager.isAdmin();
    }

    public String getUserRole() {
        return sessionManager.getRole();
    }

    public String getUserEmail() {
        return sessionManager.getEmail();
    }

    public String getUserId() {
        return sessionManager.getUserId();
    }
} 