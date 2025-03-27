package com.example.project_prm392.repository;

import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.SpecialtyResponse;
import com.example.project_prm392.network.ApiService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class SpecialtyRepository {
    private final ApiService apiService;

    @Inject
    public SpecialtyRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Call<BaseResponse<List<SpecialtyResponse>>> getAllSpecialties() {
        return apiService.getAllSpecialties();
    }

    public Call<BaseResponse<SpecialtyResponse>> getSpecialtyById(int specialtyId) {
        return apiService.getSpecialtyById(specialtyId);
    }
} 