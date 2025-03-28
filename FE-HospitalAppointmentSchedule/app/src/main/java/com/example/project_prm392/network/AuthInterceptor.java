package com.example.project_prm392.network;

import androidx.annotation.NonNull;
import com.example.project_prm392.utils.SessionManager;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    
    @Inject
    SessionManager sessionManager;

    @Override
    @NonNull
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();
        
        // Skip adding auth token for login and register endpoints
        if (originalRequest.url().toString().contains("auth/login") ||
            originalRequest.url().toString().contains("auth/register")) {
            return chain.proceed(originalRequest);
        }
        
        // Add auth token to all other requests if available
        String token = sessionManager.getAuthToken();
        if (token != null && !token.isEmpty()) {
            Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }
        
        return chain.proceed(originalRequest);
    }
} 