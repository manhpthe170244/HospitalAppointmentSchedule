package com.example.project_prm392.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_prm392.R;
import com.example.project_prm392.ui.auth.LoginActivity;
import com.example.project_prm392.utils.SessionManager;

public class SettingsActivity extends AppCompatActivity {
    private Switch notificationSwitch;
    private Switch darkModeSwitch;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize views
        notificationSwitch = findViewById(R.id.notificationSwitch);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);

        // Initialize data
        sessionManager = new SessionManager(this);

        // Load settings
        loadSettings();

        // Setup switches
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // TODO: Save notification settings
            Toast.makeText(this, "Notification settings updated", Toast.LENGTH_SHORT).show();
        });

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // TODO: Save dark mode settings
            Toast.makeText(this, "Dark mode settings updated", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadSettings() {
        // TODO: Load settings from SharedPreferences
        notificationSwitch.setChecked(true);
        darkModeSwitch.setChecked(false);
    }

    public void onLogoutClick() {
        sessionManager.clearSession();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
} 