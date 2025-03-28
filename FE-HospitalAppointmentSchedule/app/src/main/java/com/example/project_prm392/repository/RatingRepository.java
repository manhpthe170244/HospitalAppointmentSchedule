package com.example.project_prm392.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm392.api.ApiService;
import com.example.project_prm392.models.requests.RatingRequest;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.RatingResponse;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class RatingRepository {
    private final ApiService apiService;

    @Inject
    public RatingRepository(ApiService apiService) {
        this.apiService = apiService;
    }
} 