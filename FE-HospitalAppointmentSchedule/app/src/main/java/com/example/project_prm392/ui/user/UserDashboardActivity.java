package com.example.project_prm392.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.project_prm392.R;
import com.example.project_prm392.utils.SessionManager;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserDashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private CardView cardViewDoctors, cardViewServices, cardViewAppointments, cardViewBookAppointment;
    private Button btnBack;
    
    @Inject
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        
        // Initialize views
        tvWelcome = findViewById(R.id.tvWelcome);
        cardViewDoctors = findViewById(R.id.cardViewDoctors);
        cardViewServices = findViewById(R.id.cardViewServices);
        cardViewAppointments = findViewById(R.id.cardViewAppointments);
        cardViewBookAppointment = findViewById(R.id.cardViewBookAppointment);
        btnBack = findViewById(R.id.btnBack);
        
        // Set welcome message
        String userName = sessionManager.getUserName();
        if (userName != null) {
            tvWelcome.setText("Welcome, " + userName + "!");
        }
        
        // Set up click listeners
        cardViewDoctors.setOnClickListener(v -> {
            startActivity(new Intent(UserDashboardActivity.this, ViewDoctorsActivity.class));
        });
        
        cardViewServices.setOnClickListener(v -> {
            startActivity(new Intent(UserDashboardActivity.this, ViewServicesActivity.class));
        });
        
        cardViewAppointments.setOnClickListener(v -> {
            startActivity(new Intent(UserDashboardActivity.this, MyAppointmentsActivity.class));
        });
        
        cardViewBookAppointment.setOnClickListener(v -> {
            startActivity(new Intent(UserDashboardActivity.this, BookAppointmentActivity.class));
        });
        
        btnBack.setOnClickListener(v -> {
            finish();
        });
    }
} 