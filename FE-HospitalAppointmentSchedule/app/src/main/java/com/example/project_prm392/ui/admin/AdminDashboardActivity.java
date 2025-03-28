package com.example.project_prm392.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.project_prm392.R;
import com.example.project_prm392.ui.auth.LoginActivity;
import com.example.project_prm392.utils.SessionManager;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.DashboardStatsResponse;
import com.example.project_prm392.network.ApiService;
import com.example.project_prm392.network.RetrofitClient;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private TextView totalDoctorsText;
    private TextView totalPatientsText;
    private TextView totalAppointmentsText;
    private TextView totalRevenueText;
    private CardView doctorsCard;
    private CardView patientsCard;
    private CardView appointmentsCard;
    private CardView revenueCard;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        setupToolbar();
        initViews();
        setupClickListeners();
        displayWelcomeMessage();

        // Initialize API service
        apiService = RetrofitClient.getInstance().create(ApiService.class);

        // Load dashboard stats
        loadDashboardStats();
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
        totalDoctorsText = findViewById(R.id.totalDoctorsText);
        totalPatientsText = findViewById(R.id.totalPatientsText);
        totalAppointmentsText = findViewById(R.id.totalAppointmentsText);
        totalRevenueText = findViewById(R.id.totalRevenueText);
        doctorsCard = findViewById(R.id.doctorsCard);
        patientsCard = findViewById(R.id.patientsCard);
        appointmentsCard = findViewById(R.id.appointmentsCard);
        revenueCard = findViewById(R.id.revenueCard);
    }

    private void setupClickListeners() {
        cardDoctors.setOnClickListener(v -> startActivity(new Intent(this, DoctorManagementActivity.class)));
        cardSpecialties.setOnClickListener(v -> startActivity(new Intent(this, SpecialtyManagementActivity.class)));
        cardServices.setOnClickListener(v -> startActivity(new Intent(this, ServiceManagementActivity.class)));
        cardAppointments.setOnClickListener(v -> startActivity(new Intent(this, AppointmentManagementActivity.class)));
        cardUsers.setOnClickListener(v -> startActivity(new Intent(this, UserManagementActivity.class)));
        cardStatistics.setOnClickListener(v -> startActivity(new Intent(this, StatisticsActivity.class)));
        doctorsCard.setOnClickListener(v -> startActivity(new Intent(this, AdminDoctorActivity.class)));
        patientsCard.setOnClickListener(v -> startActivity(new Intent(this, AdminPatientActivity.class)));
        appointmentsCard.setOnClickListener(v -> startActivity(new Intent(this, AdminAppointmentActivity.class)));
        revenueCard.setOnClickListener(v -> startActivity(new Intent(this, AdminRevenueActivity.class)));
    }

    private void displayWelcomeMessage() {
        String userName = sessionManager.getUserName();
        tvWelcome.setText(String.format("Welcome, %s!", userName != null ? userName : "Admin"));
    }

    private void loadDashboardStats() {
        apiService.getDashboardStats().enqueue(new Callback<BaseResponse<DashboardStatsResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<DashboardStatsResponse>> call, Response<BaseResponse<DashboardStatsResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DashboardStatsResponse stats = response.body().getData();
                    totalDoctorsText.setText(String.valueOf(stats.getTotalDoctors()));
                    totalPatientsText.setText(String.valueOf(stats.getTotalPatients()));
                    totalAppointmentsText.setText(String.valueOf(stats.getTotalAppointments()));
                    totalRevenueText.setText(String.format("$%.2f", stats.getTotalRevenue()));
                } else {
                    Toast.makeText(AdminDashboardActivity.this, "Failed to load dashboard stats", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<DashboardStatsResponse>> call, Throwable t) {
                Toast.makeText(AdminDashboardActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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