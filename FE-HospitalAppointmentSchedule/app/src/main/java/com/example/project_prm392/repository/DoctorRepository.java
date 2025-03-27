package com.example.project_prm392.repository;

import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.DoctorDetailsResponse;
import com.example.project_prm392.models.responses.DoctorResponse;
import com.example.project_prm392.models.responses.DoctorScheduleResponse;
import com.example.project_prm392.network.ApiService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class DoctorRepository {
    private final ApiService apiService;

    @Inject
    public DoctorRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Call<BaseResponse<List<DoctorResponse>>> getAllDoctors() {
        return apiService.getAllDoctors();
    }

    public Call<BaseResponse<DoctorDetailsResponse>> getDoctorById(int doctorId) {
        return apiService.getDoctorById(doctorId);
    }

    public Call<BaseResponse<List<DoctorResponse>>> getDoctorsBySpecialty(int specialtyId) {
        return apiService.getDoctorsBySpecialty(specialtyId);
    }
    
    public Call<BaseResponse<List<DoctorResponse>>> getDoctorsByService(int serviceId) {
        return apiService.getDoctorsByService(serviceId);
    }
    
    public Call<BaseResponse<List<DoctorScheduleResponse>>> getDoctorSchedules(int doctorId) {
        return apiService.getDoctorSchedules(doctorId);
    }
} 