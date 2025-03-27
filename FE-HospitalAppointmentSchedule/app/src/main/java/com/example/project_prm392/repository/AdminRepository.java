package com.example.project_prm392.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.DoctorResponse;
import com.example.project_prm392.models.responses.ServiceResponse;
import com.example.project_prm392.models.responses.SpecialtyResponse;
import com.example.project_prm392.models.responses.StatisticsResponse;
import com.example.project_prm392.models.responses.UserResponse;
import com.example.project_prm392.network.ApiService;
import com.example.project_prm392.utils.SessionManager;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class AdminRepository {
    private final ApiService apiService;
    private final SessionManager sessionManager;

    @Inject
    public AdminRepository(ApiService apiService, SessionManager sessionManager) {
        this.apiService = apiService;
        this.sessionManager = sessionManager;
    }

    // Doctor Management
    public LiveData<BaseResponse<List<DoctorResponse>>> getAllDoctors() {
        MutableLiveData<BaseResponse<List<DoctorResponse>>> doctorsLiveData = new MutableLiveData<>();
        
        apiService.getAllDoctors().enqueue(new Callback<BaseResponse<List<DoctorResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<DoctorResponse>>> call, Response<BaseResponse<List<DoctorResponse>>> response) {
                if (response.isSuccessful()) {
                    doctorsLiveData.setValue(response.body());
                } else {
                    doctorsLiveData.setValue(new BaseResponse<>(false, "Failed to load doctors", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<DoctorResponse>>> call, Throwable t) {
                doctorsLiveData.setValue(new BaseResponse<>(false, "Network error: " + t.getMessage(), null));
            }
        });

        return doctorsLiveData;
    }

    // Specialty Management
    public LiveData<BaseResponse<List<SpecialtyResponse>>> getAllSpecialties() {
        MutableLiveData<BaseResponse<List<SpecialtyResponse>>> specialtiesLiveData = new MutableLiveData<>();
        
        apiService.getAllSpecialties().enqueue(new Callback<BaseResponse<List<SpecialtyResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<SpecialtyResponse>>> call, Response<BaseResponse<List<SpecialtyResponse>>> response) {
                if (response.isSuccessful()) {
                    specialtiesLiveData.setValue(response.body());
                } else {
                    specialtiesLiveData.setValue(new BaseResponse<>(false, "Failed to load specialties", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<SpecialtyResponse>>> call, Throwable t) {
                specialtiesLiveData.setValue(new BaseResponse<>(false, "Network error: " + t.getMessage(), null));
            }
        });

        return specialtiesLiveData;
    }

    // Service Management
    public LiveData<BaseResponse<List<ServiceResponse>>> getAllServices() {
        MutableLiveData<BaseResponse<List<ServiceResponse>>> servicesLiveData = new MutableLiveData<>();
        
        apiService.getAllServices().enqueue(new Callback<BaseResponse<List<ServiceResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ServiceResponse>>> call, Response<BaseResponse<List<ServiceResponse>>> response) {
                if (response.isSuccessful()) {
                    servicesLiveData.setValue(response.body());
                } else {
                    servicesLiveData.setValue(new BaseResponse<>(false, "Failed to load services", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<ServiceResponse>>> call, Throwable t) {
                servicesLiveData.setValue(new BaseResponse<>(false, "Network error: " + t.getMessage(), null));
            }
        });

        return servicesLiveData;
    }

    // User Management
    public LiveData<BaseResponse<List<UserResponse>>> getAllUsers() {
        MutableLiveData<BaseResponse<List<UserResponse>>> usersLiveData = new MutableLiveData<>();
        
        apiService.getAllUsers().enqueue(new Callback<BaseResponse<List<UserResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<UserResponse>>> call, Response<BaseResponse<List<UserResponse>>> response) {
                if (response.isSuccessful()) {
                    usersLiveData.setValue(response.body());
                } else {
                    usersLiveData.setValue(new BaseResponse<>(false, "Failed to load users", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<UserResponse>>> call, Throwable t) {
                usersLiveData.setValue(new BaseResponse<>(false, "Network error: " + t.getMessage(), null));
            }
        });

        return usersLiveData;
    }

    // Statistics
    public LiveData<BaseResponse<StatisticsResponse>> getStatistics() {
        MutableLiveData<BaseResponse<StatisticsResponse>> statisticsLiveData = new MutableLiveData<>();
        
        apiService.getStatistics().enqueue(new Callback<BaseResponse<StatisticsResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<StatisticsResponse>> call, Response<BaseResponse<StatisticsResponse>> response) {
                if (response.isSuccessful()) {
                    statisticsLiveData.setValue(response.body());
                } else {
                    statisticsLiveData.setValue(new BaseResponse<>(false, "Failed to load statistics", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<StatisticsResponse>> call, Throwable t) {
                statisticsLiveData.setValue(new BaseResponse<>(false, "Network error: " + t.getMessage(), null));
            }
        });

        return statisticsLiveData;
    }
} 