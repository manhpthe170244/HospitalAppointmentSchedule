package com.example.project_prm392.api.service;

import com.example.project_prm392.api.ApiResponse;
import com.example.project_prm392.model.Service;
import com.example.project_prm392.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceApiService {
    @GET(Constants.ENDPOINT_SERVICES)
    Call<ApiResponse<List<Service>>> getAllServices();

    @GET(Constants.ENDPOINT_SERVICES + "/{id}")
    Call<ApiResponse<Service>> getServiceById(@Path("id") int serviceId);

    @GET(Constants.ENDPOINT_SERVICES + "/specialty/{specialtyId}")
    Call<ApiResponse<List<Service>>> getServicesBySpecialty(@Path("specialtyId") int specialtyId);

    @GET(Constants.ENDPOINT_SERVICES + "/doctor/{doctorId}")
    Call<ApiResponse<List<Service>>> getServicesByDoctor(@Path("doctorId") int doctorId);

    @GET(Constants.ENDPOINT_SERVICES + "/search")
    Call<ApiResponse<List<Service>>> searchServices(@Query("keyword") String keyword);
} 