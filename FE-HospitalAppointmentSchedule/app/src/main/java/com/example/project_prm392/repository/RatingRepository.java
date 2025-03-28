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

    public LiveData<BaseResponse<RatingResponse>> submitDoctorRating(int doctorId, RatingRequest request) {
        MutableLiveData<BaseResponse<RatingResponse>> result = new MutableLiveData<>();
        
        apiService.submitDoctorRating(doctorId, request).enqueue(new Callback<BaseResponse<RatingResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<RatingResponse>> call, 
                                 Response<BaseResponse<RatingResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to submit rating", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<RatingResponse>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<List<RatingResponse>>> getDoctorRatings(int doctorId) {
        MutableLiveData<BaseResponse<List<RatingResponse>>> result = new MutableLiveData<>();
        
        apiService.getDoctorRatings(doctorId).enqueue(new Callback<BaseResponse<List<RatingResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<RatingResponse>>> call, 
                                 Response<BaseResponse<List<RatingResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to get doctor ratings", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<RatingResponse>>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<Double>> getDoctorAverageRating(int doctorId) {
        MutableLiveData<BaseResponse<Double>> result = new MutableLiveData<>();
        
        apiService.getDoctorAverageRating(doctorId).enqueue(new Callback<BaseResponse<Double>>() {
            @Override
            public void onResponse(Call<BaseResponse<Double>> call, Response<BaseResponse<Double>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to get average rating", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Double>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }
} 