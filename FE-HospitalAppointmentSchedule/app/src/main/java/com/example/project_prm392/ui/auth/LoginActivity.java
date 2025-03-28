package com.example.project_prm392.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm392.R;
import com.example.project_prm392.databinding.ActivityLoginBinding;
import com.example.project_prm392.ui.admin.AdminDashboardActivity;
import com.example.project_prm392.ui.user.MainActivity;
import com.example.project_prm392.utils.ValidationUtils;
import com.example.project_prm392.viewmodels.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Check if user is already logged in
        if (authViewModel.isLoggedIn()) {
            navigateToAppropriateScreen();
            finish();
            return;
        }

        setupViews();
    }

    private void setupViews() {
        setSupportActionBar(binding.toolbar);

        binding.loginButton.setOnClickListener(v -> attemptLogin());
        binding.registerButton.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void attemptLogin() {
        String email = binding.emailInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();

        // Validate input
        if (!ValidationUtils.isValidEmail(email)) {
            binding.emailLayout.setError(getString(R.string.invalid_email));
            return;
        }
        binding.emailLayout.setError(null);

        if (!ValidationUtils.isValidPassword(password)) {
            binding.passwordLayout.setError(getString(R.string.invalid_password));
            return;
        }
        binding.passwordLayout.setError(null);

        // Show loading
        setLoading(true);

        // Attempt login
        authViewModel.login(email, password).observe(this, response -> {
            setLoading(false);

            if (response.isSuccess()) {
                navigateToAppropriateScreen();
                finish();
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToAppropriateScreen() {
        Intent intent;
        if (authViewModel.isAdmin()) {
            intent = new Intent(this, AdminDashboardActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
    }

    private void setLoading(boolean isLoading) {
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.loginButton.setEnabled(!isLoading);
        binding.registerButton.setEnabled(!isLoading);
        binding.emailInput.setEnabled(!isLoading);
        binding.passwordInput.setEnabled(!isLoading);
    }
} 