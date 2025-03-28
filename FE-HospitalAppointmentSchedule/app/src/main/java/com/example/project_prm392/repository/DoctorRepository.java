package com.example.project_prm392.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm392.api.ApiResponse;
import com.example.project_prm392.api.ServiceGenerator;
import com.example.project_prm392.api.service.DoctorService;
import com.example.project_prm392.model.Doctor;
import com.example.project_prm392.model.DoctorSchedule;
import com.example.project_prm392.utils.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorRepository {
    private final DoctorService doctorService;

    public DoctorRepository() {
        doctorService = ServiceGenerator.getDoctorService();
    }

    public LiveData<Resource<List<Doctor>>> getAllDoctors() {
        MutableLiveData<Resource<List<Doctor>>> doctorsLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        doctorsLiveData.setValue(Resource.loading(null));
        
        // Gọi API lấy danh sách bác sĩ
        doctorService.getAllDoctors().enqueue(new Callback<ApiResponse<List<Doctor>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Doctor>>> call, Response<ApiResponse<List<Doctor>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Doctor>> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Thông báo thành công
                        doctorsLiveData.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        // Thông báo lỗi từ server
                        doctorsLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    doctorsLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Doctor>>> call, Throwable t) {
                // Thông báo lỗi mạng
                doctorsLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return doctorsLiveData;
    }

    public LiveData<Resource<Doctor>> getDoctorById(int doctorId) {
        MutableLiveData<Resource<Doctor>> doctorLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        doctorLiveData.setValue(Resource.loading(null));
        
        // Gọi API lấy thông tin bác sĩ theo ID
        doctorService.getDoctorById(doctorId).enqueue(new Callback<ApiResponse<Doctor>>() {
            @Override
            public void onResponse(Call<ApiResponse<Doctor>> call, Response<ApiResponse<Doctor>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Doctor> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Thông báo thành công
                        doctorLiveData.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        // Thông báo lỗi từ server
                        doctorLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    doctorLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Doctor>> call, Throwable t) {
                // Thông báo lỗi mạng
                doctorLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return doctorLiveData;
    }

    public LiveData<Resource<List<Doctor>>> getDoctorsBySpecialty(int specialtyId) {
        MutableLiveData<Resource<List<Doctor>>> doctorsLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        doctorsLiveData.setValue(Resource.loading(null));
        
        // Gọi API lấy danh sách bác sĩ theo chuyên khoa
        doctorService.getDoctorsBySpecialty(specialtyId).enqueue(new Callback<ApiResponse<List<Doctor>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Doctor>>> call, Response<ApiResponse<List<Doctor>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Doctor>> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Thông báo thành công
                        doctorsLiveData.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        // Thông báo lỗi từ server
                        doctorsLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    doctorsLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Doctor>>> call, Throwable t) {
                // Thông báo lỗi mạng
                doctorsLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return doctorsLiveData;
    }

    public LiveData<Resource<List<Doctor>>> getDoctorsByService(int serviceId) {
        MutableLiveData<Resource<List<Doctor>>> doctorsLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        doctorsLiveData.setValue(Resource.loading(null));
        
        // Gọi API lấy danh sách bác sĩ theo dịch vụ
        doctorService.getDoctorsByService(serviceId).enqueue(new Callback<ApiResponse<List<Doctor>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Doctor>>> call, Response<ApiResponse<List<Doctor>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Doctor>> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Thông báo thành công
                        doctorsLiveData.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        // Thông báo lỗi từ server
                        doctorsLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    doctorsLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Doctor>>> call, Throwable t) {
                // Thông báo lỗi mạng
                doctorsLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return doctorsLiveData;
    }

    public LiveData<Resource<List<DoctorSchedule>>> getDoctorSchedules(int doctorId) {
        MutableLiveData<Resource<List<DoctorSchedule>>> schedulesLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        schedulesLiveData.setValue(Resource.loading(null));
        
        // Gọi API lấy lịch làm việc của bác sĩ
        doctorService.getDoctorSchedules(doctorId).enqueue(new Callback<ApiResponse<List<DoctorSchedule>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<DoctorSchedule>>> call, Response<ApiResponse<List<DoctorSchedule>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<DoctorSchedule>> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Thông báo thành công
                        schedulesLiveData.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        // Thông báo lỗi từ server
                        schedulesLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    schedulesLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<DoctorSchedule>>> call, Throwable t) {
                // Thông báo lỗi mạng
                schedulesLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return schedulesLiveData;
    }

    public LiveData<Resource<List<Doctor>>> searchDoctors(String keyword) {
        MutableLiveData<Resource<List<Doctor>>> doctorsLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        doctorsLiveData.setValue(Resource.loading(null));
        
        // Gọi API tìm kiếm bác sĩ
        doctorService.searchDoctors(keyword).enqueue(new Callback<ApiResponse<List<Doctor>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Doctor>>> call, Response<ApiResponse<List<Doctor>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Doctor>> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Thông báo thành công
                        doctorsLiveData.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        // Thông báo lỗi từ server
                        doctorsLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    doctorsLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Doctor>>> call, Throwable t) {
                // Thông báo lỗi mạng
                doctorsLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return doctorsLiveData;
    }
} 