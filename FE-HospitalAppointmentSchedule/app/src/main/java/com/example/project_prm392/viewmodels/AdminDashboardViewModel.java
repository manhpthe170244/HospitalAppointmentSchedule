package com.example.project_prm392.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.DoctorResponse;
import com.example.project_prm392.models.responses.ServiceResponse;
import com.example.project_prm392.models.responses.SpecialtyResponse;
import com.example.project_prm392.models.responses.StatisticsResponse;
import com.example.project_prm392.models.responses.UserResponse;
import com.example.project_prm392.repository.AdminRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AdminDashboardViewModel extends ViewModel {
    private final AdminRepository adminRepository;

    @Inject
    public AdminDashboardViewModel(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public LiveData<BaseResponse<List<DoctorResponse>>> getDoctors() {
        return adminRepository.getAllDoctors();
    }

    public LiveData<BaseResponse<List<SpecialtyResponse>>> getSpecialties() {
        return adminRepository.getAllSpecialties();
    }

    public LiveData<BaseResponse<List<ServiceResponse>>> getServices() {
        return adminRepository.getAllServices();
    }

    public LiveData<BaseResponse<List<UserResponse>>> getUsers() {
        return adminRepository.getAllUsers();
    }

    public LiveData<BaseResponse<StatisticsResponse>> getStatistics() {
        return adminRepository.getStatistics();
    }
} 