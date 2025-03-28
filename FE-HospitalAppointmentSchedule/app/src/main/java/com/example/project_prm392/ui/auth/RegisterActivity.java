package com.example.project_prm392.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout tilUsername, tilPassword, tilEmail, tilFullName, tilPhoneNumber, tilAddress;
    private TextInputEditText etUsername, etPassword, etEmail, etFullName, etPhoneNumber, etAddress;
    private Button btnRegister;
    private TextView tvLogin;
    private ProgressBar progressBar;
    
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        // Khởi tạo ViewModel
        authViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance())
                .get(AuthViewModel.class);
        
        // Ánh xạ các view
        initViews();
        
        // Thiết lập listeners
        setupListeners();
    }
    
    private void initViews() {
        tilUsername = findViewById(R.id.tilUsername);
        tilPassword = findViewById(R.id.tilPassword);
        tilEmail = findViewById(R.id.tilEmail);
        tilFullName = findViewById(R.id.tilFullName);
        tilPhoneNumber = findViewById(R.id.tilPhoneNumber);
        tilAddress = findViewById(R.id.tilAddress);
        
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etFullName = findViewById(R.id.etFullName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etAddress = findViewById(R.id.etAddress);
        
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        progressBar = findViewById(R.id.progressBar);
    }
    
    private void setupListeners() {
        btnRegister.setOnClickListener(v -> attemptRegister());
        
        tvLogin.setOnClickListener(v -> {
            finish(); // Quay lại màn hình đăng nhập
        });
    }
    
    private void attemptRegister() {
        // Reset lỗi
        tilUsername.setError(null);
        tilPassword.setError(null);
        tilEmail.setError(null);
        tilFullName.setError(null);
        tilPhoneNumber.setError(null);
        tilAddress.setError(null);
        
        // Lấy giá trị từ input
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        
        // Kiểm tra hợp lệ
        boolean cancel = false;
        View focusView = null;
        
        if (TextUtils.isEmpty(address)) {
            tilAddress.setError("Vui lòng nhập địa chỉ");
            focusView = etAddress;
            cancel = true;
        }
        
        if (TextUtils.isEmpty(phoneNumber)) {
            tilPhoneNumber.setError("Vui lòng nhập số điện thoại");
            focusView = etPhoneNumber;
            cancel = true;
        } else if (!isPhoneNumberValid(phoneNumber)) {
            tilPhoneNumber.setError("Số điện thoại không hợp lệ");
            focusView = etPhoneNumber;
            cancel = true;
        }
        
        if (TextUtils.isEmpty(fullName)) {
            tilFullName.setError("Vui lòng nhập họ và tên");
            focusView = etFullName;
            cancel = true;
        }
        
        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Vui lòng nhập email");
            focusView = etEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            tilEmail.setError("Email không hợp lệ");
            focusView = etEmail;
            cancel = true;
        }
        
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Vui lòng nhập mật khẩu");
            focusView = etPassword;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            tilPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
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
            // Hiển thị progress và đăng ký
            showProgress(true);
            register(username, password, email, fullName, phoneNumber, address);
        }
    }
    
    private void register(String username, String password, String email, 
                          String fullName, String phoneNumber, String address) {
        authViewModel.register(username, password, email, fullName, phoneNumber, address)
                .observe(this, result -> {
                    if (result.status == Resource.Status.SUCCESS) {
                        showProgress(false);
                        navigateToMainActivity();
                    } else if (result.status == Resource.Status.ERROR) {
                        showProgress(false);
                        Toast.makeText(RegisterActivity.this, result.message, Toast.LENGTH_SHORT).show();
                    }
                    // Trạng thái LOADING được xử lý bằng cách hiển thị progress bar
                });
    }
    
    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }
    
    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.length() >= 10 && Patterns.PHONE.matcher(phoneNumber).matches();
    }
    
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!show);
    }
    
    private void navigateToMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
} 