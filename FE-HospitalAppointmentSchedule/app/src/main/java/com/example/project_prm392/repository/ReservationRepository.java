package com.example.project_prm392.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm392.api.ApiResponse;
import com.example.project_prm392.api.ServiceGenerator;
import com.example.project_prm392.api.service.ReservationService;
import com.example.project_prm392.model.Reservation;
import com.example.project_prm392.utils.Resource;
import com.example.project_prm392.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationRepository {
    private final ReservationService reservationService;
    private final SessionManager sessionManager;

    public ReservationRepository() {
        reservationService = ServiceGenerator.getReservationService();
        sessionManager = SessionManager.getInstance();
    }

    public LiveData<Resource<List<Reservation>>> getReservationsByPatient() {
        MutableLiveData<Resource<List<Reservation>>> reservationsLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        reservationsLiveData.setValue(Resource.loading(null));
        
        // Kiểm tra đăng nhập
        if (!sessionManager.isLoggedIn()) {
            reservationsLiveData.setValue(Resource.error("Người dùng chưa đăng nhập", null));
            return reservationsLiveData;
        }
        
        int patientId = sessionManager.getUserId();
        
        // Gọi API lấy danh sách đặt lịch của bệnh nhân
        reservationService.getReservationsByPatient(patientId).enqueue(new Callback<ApiResponse<List<Reservation>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Reservation>>> call, Response<ApiResponse<List<Reservation>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Reservation>> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Thông báo thành công
                        reservationsLiveData.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        // Thông báo lỗi từ server
                        reservationsLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    reservationsLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Reservation>>> call, Throwable t) {
                // Thông báo lỗi mạng
                reservationsLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return reservationsLiveData;
    }

    public LiveData<Resource<Reservation>> getReservationById(int reservationId) {
        MutableLiveData<Resource<Reservation>> reservationLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        reservationLiveData.setValue(Resource.loading(null));
        
        // Gọi API lấy thông tin đặt lịch theo ID
        reservationService.getReservationById(reservationId).enqueue(new Callback<ApiResponse<Reservation>>() {
            @Override
            public void onResponse(Call<ApiResponse<Reservation>> call, Response<ApiResponse<Reservation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Reservation> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Thông báo thành công
                        reservationLiveData.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        // Thông báo lỗi từ server
                        reservationLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    reservationLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Reservation>> call, Throwable t) {
                // Thông báo lỗi mạng
                reservationLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return reservationLiveData;
    }

    public LiveData<Resource<Reservation>> createReservation(Reservation reservation) {
        MutableLiveData<Resource<Reservation>> reservationLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        reservationLiveData.setValue(Resource.loading(null));
        
        // Gọi API tạo đặt lịch mới
        reservationService.createReservation(reservation).enqueue(new Callback<ApiResponse<Reservation>>() {
            @Override
            public void onResponse(Call<ApiResponse<Reservation>> call, Response<ApiResponse<Reservation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Reservation> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Thông báo thành công
                        reservationLiveData.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        // Thông báo lỗi từ server
                        reservationLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    reservationLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Reservation>> call, Throwable t) {
                // Thông báo lỗi mạng
                reservationLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return reservationLiveData;
    }

    public LiveData<Resource<Reservation>> updateReservation(Reservation reservation) {
        MutableLiveData<Resource<Reservation>> reservationLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        reservationLiveData.setValue(Resource.loading(null));
        
        // Gọi API cập nhật đặt lịch
        reservationService.updateReservation(reservation.getReservationId(), reservation).enqueue(new Callback<ApiResponse<Reservation>>() {
            @Override
            public void onResponse(Call<ApiResponse<Reservation>> call, Response<ApiResponse<Reservation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Reservation> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Thông báo thành công
                        reservationLiveData.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        // Thông báo lỗi từ server
                        reservationLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    reservationLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Reservation>> call, Throwable t) {
                // Thông báo lỗi mạng
                reservationLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return reservationLiveData;
    }

    public LiveData<Resource<Reservation>> updateReservationStatus(int reservationId, String status) {
        MutableLiveData<Resource<Reservation>> reservationLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        reservationLiveData.setValue(Resource.loading(null));
        
        // Gọi API cập nhật trạng thái đặt lịch
        reservationService.updateReservationStatus(reservationId, status).enqueue(new Callback<ApiResponse<Reservation>>() {
            @Override
            public void onResponse(Call<ApiResponse<Reservation>> call, Response<ApiResponse<Reservation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Reservation> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Thông báo thành công
                        reservationLiveData.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        // Thông báo lỗi từ server
                        reservationLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    reservationLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Reservation>> call, Throwable t) {
                // Thông báo lỗi mạng
                reservationLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return reservationLiveData;
    }

    public LiveData<Resource<Void>> cancelReservation(int reservationId) {
        MutableLiveData<Resource<Void>> resultLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        resultLiveData.setValue(Resource.loading(null));
        
        // Gọi API xóa đặt lịch
        reservationService.deleteReservation(reservationId).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Void> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess()) {
                        // Thông báo thành công
                        resultLiveData.setValue(Resource.success(null));
                    } else {
                        // Thông báo lỗi từ server
                        resultLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    resultLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                // Thông báo lỗi mạng
                resultLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return resultLiveData;
    }
} 