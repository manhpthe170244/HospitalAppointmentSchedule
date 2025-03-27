package com.example.project_prm392.models.responses;

import com.google.gson.annotations.SerializedName;

public class BaseResponse<T> {
    @SerializedName("success")
    private boolean isSuccess;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("data")
    private T data;

    // Default constructor for Gson
    public BaseResponse() {
    }
    
    // Constructor for success/error responses
    public BaseResponse(boolean success, String message, T data) {
        this.isSuccess = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
} 