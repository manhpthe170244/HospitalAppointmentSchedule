package com.example.project_prm392.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm392.api.ApiService;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.NotificationResponse;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class NotificationRepository {
    private final ApiService apiService;

    @Inject
    public NotificationRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<BaseResponse<List<NotificationResponse>>> getUserNotifications() {
        MutableLiveData<BaseResponse<List<NotificationResponse>>> result = new MutableLiveData<>();
        
        apiService.getUserNotifications().enqueue(new Callback<BaseResponse<List<NotificationResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<NotificationResponse>>> call, 
                                 Response<BaseResponse<List<NotificationResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to get notifications", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<NotificationResponse>>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<List<NotificationResponse>>> getUnreadNotifications() {
        MutableLiveData<BaseResponse<List<NotificationResponse>>> result = new MutableLiveData<>();
        
        apiService.getUnreadNotifications().enqueue(new Callback<BaseResponse<List<NotificationResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<NotificationResponse>>> call, 
                                 Response<BaseResponse<List<NotificationResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to get unread notifications", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<NotificationResponse>>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<NotificationResponse>> markNotificationAsRead(int notificationId) {
        MutableLiveData<BaseResponse<NotificationResponse>> result = new MutableLiveData<>();
        
        apiService.markNotificationAsRead(notificationId).enqueue(new Callback<BaseResponse<NotificationResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<NotificationResponse>> call, 
                                 Response<BaseResponse<NotificationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to mark notification as read", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<NotificationResponse>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<Void>> markAllNotificationsAsRead() {
        MutableLiveData<BaseResponse<Void>> result = new MutableLiveData<>();
        
        apiService.markAllNotificationsAsRead().enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to mark all notifications as read", null));
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Void>> call, Throwable t) {
                result.setValue(new BaseResponse<>(false, t.getMessage(), null));
            }
        });
        
        return result;
    }

    public LiveData<BaseResponse<Void>> deleteNotification(int notificationId) {
        MutableLiveData<BaseResponse<Void>> result = new MutableLiveData<>();
        
        apiService.deleteNotification(notificationId).enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(response.body());
                } else {
                    result.setValue(new BaseResponse<>(false, "Failed to delete notification", null));
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