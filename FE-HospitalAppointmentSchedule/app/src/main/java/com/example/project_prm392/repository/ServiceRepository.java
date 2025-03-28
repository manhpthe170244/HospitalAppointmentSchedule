package com.example.project_prm392.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm392.api.ApiResponse;
import com.example.project_prm392.api.ServiceGenerator;
import com.example.project_prm392.api.service.ServiceApiService;
import com.example.project_prm392.model.Service;
import com.example.project_prm392.utils.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceRepository {
    private final ServiceApiService serviceApiService;

    public ServiceRepository() {
        serviceApiService = ServiceGenerator.getServiceApiService();
    }

    public LiveData<Resource<List<Service>>> getAllServices() {
        MutableLiveData<Resource<List<Service>>> servicesLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        servicesLiveData.setValue(Resource.loading(null));
        
        // Gọi API lấy danh sách dịch vụ
        serviceApiService.getAllServices().enqueue(new Callback<ApiResponse<List<Service>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Service>>> call, Response<ApiResponse<List<Service>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Service>> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Thông báo thành công
                        servicesLiveData.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        // Thông báo lỗi từ server
                        servicesLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    servicesLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Service>>> call, Throwable t) {
                // Thông báo lỗi mạng
                servicesLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return servicesLiveData;
    }

    public LiveData<Resource<Service>> getServiceById(int serviceId) {
        MutableLiveData<Resource<Service>> serviceLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        serviceLiveData.setValue(Resource.loading(null));
        
        // Gọi API lấy thông tin dịch vụ theo ID
        serviceApiService.getServiceById(serviceId).enqueue(new Callback<ApiResponse<Service>>() {
            @Override
            public void onResponse(Call<ApiResponse<Service>> call, Response<ApiResponse<Service>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Service> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Thông báo thành công
                        serviceLiveData.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        // Thông báo lỗi từ server
                        serviceLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    serviceLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Service>> call, Throwable t) {
                // Thông báo lỗi mạng
                serviceLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return serviceLiveData;
    }

    public LiveData<Resource<List<Service>>> getServicesBySpecialty(int specialtyId) {
        MutableLiveData<Resource<List<Service>>> servicesLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        servicesLiveData.setValue(Resource.loading(null));
        
        // Gọi API lấy danh sách dịch vụ theo chuyên khoa
        serviceApiService.getServicesBySpecialty(specialtyId).enqueue(new Callback<ApiResponse<List<Service>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Service>>> call, Response<ApiResponse<List<Service>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Service>> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Thông báo thành công
                        servicesLiveData.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        // Thông báo lỗi từ server
                        servicesLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    servicesLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Service>>> call, Throwable t) {
                // Thông báo lỗi mạng
                servicesLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return servicesLiveData;
    }

    public LiveData<Resource<List<Service>>> getServicesByDoctor(int doctorId) {
        MutableLiveData<Resource<List<Service>>> servicesLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        servicesLiveData.setValue(Resource.loading(null));
        
        // Gọi API lấy danh sách dịch vụ theo bác sĩ
        serviceApiService.getServicesByDoctor(doctorId).enqueue(new Callback<ApiResponse<List<Service>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Service>>> call, Response<ApiResponse<List<Service>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Service>> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Thông báo thành công
                        servicesLiveData.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        // Thông báo lỗi từ server
                        servicesLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    servicesLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Service>>> call, Throwable t) {
                // Thông báo lỗi mạng
                servicesLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return servicesLiveData;
    }

    public LiveData<Resource<List<Service>>> searchServices(String keyword) {
        MutableLiveData<Resource<List<Service>>> servicesLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        servicesLiveData.setValue(Resource.loading(null));
        
        // Gọi API tìm kiếm dịch vụ
        serviceApiService.searchServices(keyword).enqueue(new Callback<ApiResponse<List<Service>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Service>>> call, Response<ApiResponse<List<Service>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Service>> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Thông báo thành công
                        servicesLiveData.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        // Thông báo lỗi từ server
                        servicesLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    servicesLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Service>>> call, Throwable t) {
                // Thông báo lỗi mạng
                servicesLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return servicesLiveData;
    }
} 