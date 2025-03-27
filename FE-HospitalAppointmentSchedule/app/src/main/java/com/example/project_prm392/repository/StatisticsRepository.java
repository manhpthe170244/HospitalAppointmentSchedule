package com.example.project_prm392.repository;

import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.StatisticsResponse;
import com.example.project_prm392.network.ApiService;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class StatisticsRepository {
    private final ApiService apiService;

    @Inject
    public StatisticsRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Call<BaseResponse<StatisticsResponse>> getDashboardStatistics() {
        return apiService.getDashboardStatistics();
    }
} 