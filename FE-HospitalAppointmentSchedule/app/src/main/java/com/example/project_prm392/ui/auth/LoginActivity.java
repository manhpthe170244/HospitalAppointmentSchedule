package com.example.project_prm392.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm392.MainActivity;
import com.example.project_prm392.R;
import com.example.project_prm392.utils.Resource;
import com.example.project_prm392.viewmodel.AuthViewModel;
import com.example.project_prm392.viewmodel.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout tilUsername, tilPassword;
    private TextInputEditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private ProgressBar progressBar;
    
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Khởi tạo ViewModel
        authViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance())
                .get(AuthViewModel.class);
        
        // Kiểm tra người dùng đã đăng nhập hay chưa
        if (authViewModel.isLoggedIn()) {
            navigateToMainActivity();
            return;
        }
        
        // Ánh xạ các view
        initViews();
        
        // Thiết lập listeners
        setupListeners();
    }
    
    private void initViews() {
        tilUsername = findViewById(R.id.tilUsername);
        tilPassword = findViewById(R.id.tilPassword);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        progressBar = findViewById(R.id.progressBar);
    }
    
    private void setupListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());
        
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
    
    private void attemptLogin() {
        // Reset lỗi
        tilUsername.setError(null);
        tilPassword.setError(null);
        
        // Lấy giá trị từ input
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        
        // Kiểm tra hợp lệ
        boolean cancel = false;
        View focusView = null;
        
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Vui lòng nhập mật khẩu");
            focusView = etPassword;
            cancel = true;
        }
        
        if (TextUtils.isEmpty(username)) {
            tilUsername.setError("Vui lòng nhập tên đăng nhập");
            focusView = etUsername;
            cancel = true;
        }
        
        if (cancel) {
            // Có lỗi, focus vào trường bị lỗi đầu tiên
            focusView.requestFocus();
        } else {
            // Hiển thị progress và đăng nhập
            showProgress(true);
            login(username, password);
        }
    }
    
    private void login(String username, String password) {
        authViewModel.login(username, password).observe(this, result -> {
            if (result.status == Resource.Status.SUCCESS) {
                showProgress(false);
                navigateToMainActivity();
            } else if (result.status == Resource.Status.ERROR) {
                showProgress(false);
                Toast.makeText(LoginActivity.this, result.message, Toast.LENGTH_SHORT).show();
            }
            // Trạng thái LOADING được xử lý bằng cách hiển thị progress bar
        });
    }
    
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
    }
    
    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
} 