package com.example.project_prm392.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm392.api.ApiService;
import com.example.project_prm392.models.requests.CreateDoctorRequest;
import com.example.project_prm392.models.requests.UpdateDoctorRequest;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.DoctorResponse;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class DoctorRepository {
    private final ApiService apiService;

    @Inject
    public DoctorRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<BaseResponse<List<DoctorResponse>>> getAllDoctors() {
        MutableLiveData<BaseResponse<List<DoctorResponse>>> result = new MutableLiveData<>();

        apiService.getAllDoctors().enqueue(new Callback<BaseResponse<List<DoctorResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<DoctorResponse>>> call, Response<BaseResponse<List<DoctorResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to get doctors", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<DoctorResponse>>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<BaseResponse<DoctorResponse>> getDoctorById(int doctorId) {
        MutableLiveData<BaseResponse<DoctorResponse>> result = new MutableLiveData<>();

        apiService.getDoctorById(doctorId).enqueue(new Callback<BaseResponse<DoctorResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<DoctorResponse>> call, Response<BaseResponse<DoctorResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to get doctor", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<DoctorResponse>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<BaseResponse<DoctorResponse>> createDoctor(CreateDoctorRequest request) {
        MutableLiveData<BaseResponse<DoctorResponse>> result = new MutableLiveData<>();

        apiService.createDoctor(request).enqueue(new Callback<BaseResponse<DoctorResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<DoctorResponse>> call, Response<BaseResponse<DoctorResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to create doctor", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<DoctorResponse>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<BaseResponse<DoctorResponse>> updateDoctor(UpdateDoctorRequest request) {
        MutableLiveData<BaseResponse<DoctorResponse>> result = new MutableLiveData<>();

        apiService.updateDoctor(request).enqueue(new Callback<BaseResponse<DoctorResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<DoctorResponse>> call, Response<BaseResponse<DoctorResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to update doctor", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<DoctorResponse>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });

        return result;
    }

    public LiveData<BaseResponse<Void>> deleteDoctor(int doctorId) {
        MutableLiveData<BaseResponse<Void>> result = new MutableLiveData<>();

        apiService.deleteDoctor(doctorId).enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to delete doctor", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Void>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });

        return result;
    }
} 