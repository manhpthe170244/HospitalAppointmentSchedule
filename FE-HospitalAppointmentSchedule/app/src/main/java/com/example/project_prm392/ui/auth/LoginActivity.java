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

import com.example.project_prm392.MainActivity;
import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.LoginResponse;
import com.example.project_prm392.repository.AuthRepository;
import com.example.project_prm392.ui.admin.AdminDashboardActivity;
import com.example.project_prm392.ui.user.UserDashboardActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private ProgressBar progressBar;
    
    @Inject
    AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        progressBar = findViewById(R.id.progressBar);
        
        // Check if user is already logged in
        if (authRepository.isLoggedIn()) {
            navigateToAppropriateScreen();
            finish();
            return;
        }
        
        // Set up click listeners
        btnLogin.setOnClickListener(v -> loginUser());
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
    
    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        // Validate input
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
        
        // Show progress
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
        
        // Call login API
        authRepository.login(email, password).observe(this, result -> {
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
            
            if (result != null) {
                if (result.isSuccess() && result.getData() != null) {
                    // Navigate to appropriate screen
                    navigateToAppropriateScreen();
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, result.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }
    
    private void navigateToAppropriateScreen() {
        String userRole = authRepository.getUserRole();
        
        if (userRole != null) {
            if (userRole.equalsIgnoreCase("Admin")) {
                startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
            } else if (userRole.equalsIgnoreCase("Doctor")) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else if (userRole.equalsIgnoreCase("Patient")) {
                startActivity(new Intent(LoginActivity.this, UserDashboardActivity.class));
            } else {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        } else {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }
} 