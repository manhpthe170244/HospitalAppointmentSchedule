package com.example.project_prm392.ui.profile;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm392.R;
import com.example.project_prm392.utils.Resource;
import com.example.project_prm392.viewmodel.UserViewModel;
import com.example.project_prm392.viewmodel.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ChangePasswordActivity extends AppCompatActivity {
    private TextInputLayout tilCurrentPassword, tilNewPassword, tilConfirmPassword;
    private TextInputEditText etCurrentPassword, etNewPassword, etConfirmPassword;
    private Button btnUpdatePassword;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Khởi tạo ViewModel
        userViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance())
                .get(UserViewModel.class);

        // Khởi tạo Views
        initViews();

        // Thiết lập Toolbar
        setupToolbar();

        // Thiết lập listeners
        setupListeners();
    }

    private void initViews() {
        tilCurrentPassword = findViewById(R.id.tilCurrentPassword);
        tilNewPassword = findViewById(R.id.tilNewPassword);
        tilConfirmPassword = findViewById(R.id.tilConfirmPassword);
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupListeners() {
        btnUpdatePassword.setOnClickListener(v -> attemptChangePassword());
    }

    private void attemptChangePassword() {
        // Reset lỗi
        tilCurrentPassword.setError(null);
        tilNewPassword.setError(null);
        tilConfirmPassword.setError(null);

        // Lấy giá trị từ input
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Kiểm tra hợp lệ
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(currentPassword)) {
            tilCurrentPassword.setError("Vui lòng nhập mật khẩu hiện tại");
            focusView = etCurrentPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(newPassword)) {
            tilNewPassword.setError("Vui lòng nhập mật khẩu mới");
            focusView = etNewPassword;
            cancel = true;
        } else if (newPassword.length() < 6) {
            tilNewPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            focusView = etNewPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            tilConfirmPassword.setError("Vui lòng xác nhận mật khẩu mới");
            focusView = etConfirmPassword;
            cancel = true;
        } else if (!confirmPassword.equals(newPassword)) {
            tilConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            focusView = etConfirmPassword;
            cancel = true;
        }

        if (cancel) {
            // Có lỗi, focus vào trường bị lỗi đầu tiên
            focusView.requestFocus();
        } else {
            // Không có lỗi, tiến hành đổi mật khẩu
            changePassword(currentPassword, newPassword);
        }
    }

    private void changePassword(String currentPassword, String newPassword) {
        showLoading(true);

        userViewModel.changePassword(currentPassword, newPassword).observe(this, result -> {
            showLoading(false);

            if (result.status == Resource.Status.SUCCESS) {
                Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                finish();
            } else if (result.status == Resource.Status.ERROR) {
                if (result.message.contains("incorrect")) {
                    tilCurrentPassword.setError("Mật khẩu hiện tại không đúng");
                    etCurrentPassword.requestFocus();
                } else {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        if (isLoading) {
            btnUpdatePassword.setEnabled(false);
        } else {
            btnUpdatePassword.setEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 