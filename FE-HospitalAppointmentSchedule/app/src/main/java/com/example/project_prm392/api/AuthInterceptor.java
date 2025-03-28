package com.example.project_prm392.api;

import android.text.TextUtils;

import com.example.project_prm392.utils.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        
        // Get token from SessionManager
        String token = SessionManager.getInstance().getAuthToken();
        
        // If token exists, add it to the request header
        if (!TextUtils.isEmpty(token)) {
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .method(original.method(), original.body());
            
            // Add other common headers
            requestBuilder.header("Content-Type", "application/json");
            requestBuilder.header("Accept", "application/json");
            
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
        
        return chain.proceed(original);
    }
} 