package com.example.project_prm392.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.project_prm392.api.ApiResponse;
import com.example.project_prm392.api.ServiceGenerator;
import com.example.project_prm392.api.service.UserService;
import com.example.project_prm392.api.service.PasswordChangeRequest;
import com.example.project_prm392.model.User;
import com.example.project_prm392.utils.Resource;
import com.example.project_prm392.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final UserService userService;
    private final SessionManager sessionManager;

    public UserRepository() {
        userService = ServiceGenerator.getUserService();
        sessionManager = SessionManager.getInstance();
    }

    public LiveData<Resource<User>> getCurrentUserProfile() {
        MutableLiveData<Resource<User>> userLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        userLiveData.setValue(Resource.loading(null));
        
        // Kiểm tra đăng nhập
        if (!sessionManager.isLoggedIn()) {
            userLiveData.setValue(Resource.error("Người dùng chưa đăng nhập", null));
            return userLiveData;
        }
        
        // Gọi API lấy thông tin người dùng
        userService.getCurrentUserProfile().enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<User> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Thông báo thành công
                        userLiveData.setValue(Resource.success(apiResponse.getData()));
                        
                        // Cập nhật thông tin người dùng trong session
                        sessionManager.saveUser(apiResponse.getData());
                    } else {
                        // Thông báo lỗi từ server
                        userLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    userLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                // Thông báo lỗi mạng
                userLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return userLiveData;
    }

    public LiveData<Resource<User>> getUserById(int userId) {
        MutableLiveData<Resource<User>> userLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        userLiveData.setValue(Resource.loading(null));
        
        // Gọi API lấy thông tin người dùng theo ID
        userService.getUserById(userId).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<User> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Thông báo thành công
                        userLiveData.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        // Thông báo lỗi từ server
                        userLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    userLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                // Thông báo lỗi mạng
                userLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return userLiveData;
    }

    public LiveData<Resource<User>> updateUser(User user) {
        MutableLiveData<Resource<User>> userLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        userLiveData.setValue(Resource.loading(null));
        
        // Gọi API cập nhật thông tin người dùng
        userService.updateUser(user.getUserId(), user).enqueue(new Callback<ApiResponse<User>>() {
            @Override
            public void onResponse(Call<ApiResponse<User>> call, Response<ApiResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<User> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                        // Cập nhật thông tin người dùng trong session
                        sessionManager.saveUser(apiResponse.getData());
                        
                        // Thông báo thành công
                        userLiveData.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        // Thông báo lỗi từ server
                        userLiveData.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    userLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<User>> call, Throwable t) {
                // Thông báo lỗi mạng
                userLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), null));
            }
        });
        
        return userLiveData;
    }

    public User getLocalUser() {
        return sessionManager.getUser();
    }

    public LiveData<Resource<Boolean>> changePassword(String currentPassword, String newPassword) {
        MutableLiveData<Resource<Boolean>> resultLiveData = new MutableLiveData<>();
        
        // Bắt đầu loading
        resultLiveData.setValue(Resource.loading(null));
        
        // Kiểm tra đăng nhập
        if (!sessionManager.isLoggedIn()) {
            resultLiveData.setValue(Resource.error("Người dùng chưa đăng nhập", null));
            return resultLiveData;
        }
        
        // Tạo request
        PasswordChangeRequest request = new PasswordChangeRequest(currentPassword, newPassword);
        
        // Gọi API đổi mật khẩu
        userService.changePassword(request).enqueue(new Callback<ApiResponse<Boolean>>() {
            @Override
            public void onResponse(Call<ApiResponse<Boolean>> call, Response<ApiResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Boolean> apiResponse = response.body();
                    
                    if (apiResponse.isSuccess() && apiResponse.getData() != null && apiResponse.getData()) {
                        // Thông báo thành công
                        resultLiveData.setValue(Resource.success(true));
                    } else {
                        // Thông báo lỗi từ server
                        resultLiveData.setValue(Resource.error(apiResponse.getMessage(), false));
                    }
                } else {
                    // Thông báo lỗi HTTP
                    resultLiveData.setValue(Resource.error("Lỗi kết nối: " + response.code(), false));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Boolean>> call, Throwable t) {
                // Thông báo lỗi mạng
                resultLiveData.setValue(Resource.error("Lỗi mạng: " + t.getMessage(), false));
            }
        });
        
        return resultLiveData;
    }

    // Singleton pattern
    private static UserRepository instance;
    
    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }
} 