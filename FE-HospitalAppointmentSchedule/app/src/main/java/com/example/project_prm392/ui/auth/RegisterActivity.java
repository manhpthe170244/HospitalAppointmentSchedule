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
import androidx.lifecycle.Observer;

import com.example.project_prm392.R;
import com.example.project_prm392.models.requests.RegisterRequest;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.UserResponse;
import com.example.project_prm392.repository.AuthRepository;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {
    
    @Inject
    AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        // Initialize views
        EditText etName = findViewById(R.id.etName);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etConfirmPassword = findViewById(R.id.etConfirmPassword);
        EditText etPhone = findViewById(R.id.etPhone);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView tvLogin = findViewById(R.id.tvLogin);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        
        // Set up click listeners
        btnRegister.setOnClickListener(view -> registerUser(
            etName, etEmail, etPassword, etConfirmPassword, etPhone, btnRegister, progressBar
        ));
        
        tvLogin.setOnClickListener(view -> finish());
    }
    
    private void registerUser(
        EditText etName, 
        EditText etEmail, 
        EditText etPassword, 
        EditText etConfirmPassword,
        EditText etPhone,
        Button btnRegister,
        ProgressBar progressBar
    ) {
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
        
        // Call register API and observe result
        authRepository.register(registerRequest).observe(this, response -> {
            progressBar.setVisibility(View.GONE);
            btnRegister.setEnabled(true);
            
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "Registration successful. Please login.", Toast.LENGTH_LONG).show();
                
                // Navigate to login screen
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                String message = response != null ? response.getMessage() : "Registration failed. Please try again.";
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
} 