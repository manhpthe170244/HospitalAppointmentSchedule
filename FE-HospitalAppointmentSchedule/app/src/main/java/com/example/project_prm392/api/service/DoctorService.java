package com.example.project_prm392.api.service;

import com.example.project_prm392.api.ApiResponse;
import com.example.project_prm392.model.Doctor;
import com.example.project_prm392.model.DoctorSchedule;
import com.example.project_prm392.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DoctorService {
    @GET(Constants.ENDPOINT_DOCTORS)
    Call<ApiResponse<List<Doctor>>> getAllDoctors();

    @GET(Constants.ENDPOINT_DOCTORS + "/{id}")
    Call<ApiResponse<Doctor>> getDoctorById(@Path("id") int doctorId);

    @GET(Constants.ENDPOINT_DOCTORS + "/specialty/{specialtyId}")
    Call<ApiResponse<List<Doctor>>> getDoctorsBySpecialty(@Path("specialtyId") int specialtyId);

    @GET(Constants.ENDPOINT_DOCTORS + "/service/{serviceId}")
    Call<ApiResponse<List<Doctor>>> getDoctorsByService(@Path("serviceId") int serviceId);

    @GET(Constants.ENDPOINT_DOCTORS + "/{id}/schedules")
    Call<ApiResponse<List<DoctorSchedule>>> getDoctorSchedules(@Path("id") int doctorId);

    @GET(Constants.ENDPOINT_DOCTORS + "/search")
    Call<ApiResponse<List<Doctor>>> searchDoctors(@Query("keyword") String keyword);
} 