package com.example.project_prm392.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm392.R;
import com.example.project_prm392.models.requests.RegisterRequest;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.UserResponse;
import com.example.project_prm392.repository.AuthRepository;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {
    
    private EditText etName, etEmail, etPassword, etConfirmPassword, etPhone;
    private Button btnRegister;
    private TextView tvLogin;
    private ProgressBar progressBar;
    
    @Inject
    AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        // Initialize views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etPhone);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        progressBar = findViewById(R.id.progressBar);
        
        // Set up click listeners
        btnRegister.setOnClickListener(v -> registerUser());
        tvLogin.setOnClickListener(v -> {
            finish();
        });
    }
    
    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        
        // Validate input
        if (name.isEmpty()) {
            etName.setError("Name is required");
            etName.requestFocus();
            return;
        }
        
        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }
        
        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Confirm password is required");
            etConfirmPassword.requestFocus();
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return;
        }
        
        if (phone.isEmpty()) {
            etPhone.setError("Phone number is required");
            etPhone.requestFocus();
            return;
        }
        
        // Show progress
        progressBar.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);
        
        // Create register request
        RegisterRequest registerRequest = new RegisterRequest(email, password, name, phone);
        
        // Call register API
        authRepository.register(registerRequest).enqueue(new Callback<BaseResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<UserResponse>> call, Response<BaseResponse<UserResponse>> response) {
                progressBar.setVisibility(View.GONE);
                btnRegister.setEnabled(true);
                
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<UserResponse> baseResponse = response.body();
                    
                    if (baseResponse.isSuccess()) {
                        Toast.makeText(RegisterActivity.this, "Registration successful. Please login.", Toast.LENGTH_LONG).show();
                        
                        // Navigate to login screen
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, baseResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed. Please try again.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<UserResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnRegister.setEnabled(true);
                Toast.makeText(RegisterActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
} 