package com.example.project_prm392.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.project_prm392.R;
import com.example.project_prm392.ui.auth.LoginActivity;
import com.example.project_prm392.utils.SessionManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AdminDashboardActivity extends AppCompatActivity {

    @Inject
    SessionManager sessionManager;

    private TextView tvWelcome;
    private CardView cardDoctors;
    private CardView cardSpecialties;
    private CardView cardServices;
    private CardView cardAppointments;
    private CardView cardUsers;
    private CardView cardStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        setupToolbar();
        initViews();
        setupClickListeners();
        displayWelcomeMessage();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Admin Dashboard");
        }
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        cardDoctors = findViewById(R.id.cardDoctors);
        cardSpecialties = findViewById(R.id.cardSpecialties);
        cardServices = findViewById(R.id.cardServices);
        cardAppointments = findViewById(R.id.cardAppointments);
        cardUsers = findViewById(R.id.cardUsers);
        cardStatistics = findViewById(R.id.cardStatistics);
    }

    private void setupClickListeners() {
        cardDoctors.setOnClickListener(v -> startActivity(new Intent(this, DoctorManagementActivity.class)));
        cardSpecialties.setOnClickListener(v -> startActivity(new Intent(this, SpecialtyManagementActivity.class)));
        cardServices.setOnClickListener(v -> startActivity(new Intent(this, ServiceManagementActivity.class)));
        cardAppointments.setOnClickListener(v -> startActivity(new Intent(this, AppointmentManagementActivity.class)));
        cardUsers.setOnClickListener(v -> startActivity(new Intent(this, UserManagementActivity.class)));
        cardStatistics.setOnClickListener(v -> startActivity(new Intent(this, StatisticsActivity.class)));
    }

    private void displayWelcomeMessage() {
        String userName = sessionManager.getUserName();
        tvWelcome.setText(String.format("Welcome, %s!", userName != null ? userName : "Admin"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        sessionManager.clearSession();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
} 