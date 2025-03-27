package com.example.project_prm392.repository;

import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.ServiceResponse;
import com.example.project_prm392.network.ApiService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class ServiceRepository {
    private final ApiService apiService;

    @Inject
    public ServiceRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Call<BaseResponse<List<ServiceResponse>>> getAllServices() {
        return apiService.getAllServices();
    }

    public Call<BaseResponse<ServiceResponse>> getServiceById(int serviceId) {
        return apiService.getServiceById(serviceId);
    }

    public Call<BaseResponse<List<ServiceResponse>>> getServicesBySpecialty(int specialtyId) {
        return apiService.getServicesBySpecialty(specialtyId);
    }
} 