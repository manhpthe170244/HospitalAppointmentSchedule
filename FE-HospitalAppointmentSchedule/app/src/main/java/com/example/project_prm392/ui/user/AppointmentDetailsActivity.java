package com.example.project_prm392.ui.user;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.ReservationDetailsResponse;
import com.example.project_prm392.models.responses.ReservationResponse;
import com.example.project_prm392.network.ApiService;
import com.example.project_prm392.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class AppointmentDetailsActivity extends AppCompatActivity {

    @Inject
    ApiService apiService;
    
    private int reservationId;
    private ProgressBar progressBar;
    private TextView tvDoctorName, tvSpecialty, tvDate, tvTime, tvStatus, tvReason, tvRoom;
    private Button btnCancel;
    private ReservationDetailsResponse reservation;
    
    private final SimpleDateFormat inputFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.getDefault());
    private final SimpleDateFormat outputDateFormat = new SimpleDateFormat(Constants.DISPLAY_DATE_FORMAT, Locale.getDefault());
    private final SimpleDateFormat outputTimeFormat = new SimpleDateFormat(Constants.DISPLAY_TIME_FORMAT, Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        
        // Get reservation ID from intent
        reservationId = getIntent().getIntExtra("reservationId", -1);
        if (reservationId == -1) {
            Toast.makeText(this, "Invalid appointment ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Appointment Details");
        
        // Initialize views
        progressBar = findViewById(R.id.progressBar);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvSpecialty = findViewById(R.id.tvSpecialty);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvStatus = findViewById(R.id.tvStatus);
        tvReason = findViewById(R.id.tvReason);
        tvRoom = findViewById(R.id.tvRoom);
        btnCancel = findViewById(R.id.btnCancel);
        
        // Set up cancel button
        btnCancel.setOnClickListener(v -> showCancellationDialog());
        
        // Load reservation details
        loadReservationDetails();
    }
    
    private void loadReservationDetails() {
        progressBar.setVisibility(View.VISIBLE);
        
        apiService.getReservationDetails(reservationId).enqueue(new Callback<BaseResponse<ReservationDetailsResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<ReservationDetailsResponse>> call, Response<BaseResponse<ReservationDetailsResponse>> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    reservation = response.body().getData();
                    displayReservationDetails(reservation);
                } else {
                    Toast.makeText(AppointmentDetailsActivity.this, "Error loading appointment details", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            
            @Override
            public void onFailure(Call<BaseResponse<ReservationDetailsResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AppointmentDetailsActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    
    private void displayReservationDetails(ReservationDetailsResponse reservation) {
        tvDoctorName.setText(reservation.getDoctorName());
        tvSpecialty.setText(reservation.getServiceName());
        
        // Format date and time
        try {
            Date appointmentDate = inputFormat.parse(reservation.getAppointmentDate());
            if (appointmentDate != null) {
                tvDate.setText(outputDateFormat.format(appointmentDate));
                tvTime.setText(outputTimeFormat.format(appointmentDate));
            }
        } catch (ParseException e) {
            tvDate.setText(reservation.getAppointmentDate());
            tvTime.setText("");
        }
        
        // Set reason
        if (reservation.getReason() != null && !reservation.getReason().isEmpty()) {
            tvReason.setText(reservation.getReason());
        } else {
            tvReason.setText("No reason provided");
        }
        
        // Set room
        tvRoom.setText(reservation.getRoomName());
        
        // Set status with color
        tvStatus.setText(reservation.getStatus());
        
        int statusColor = switch (reservation.getStatus().toLowerCase()) {
            case "pending" -> R.color.statusPending;
            case "approved" -> R.color.statusApproved;
            case "completed" -> R.color.statusCompleted;
            case "cancelled" -> R.color.statusCancelled;
            default -> R.color.statusPending;
        };
        
        tvStatus.setTextColor(ContextCompat.getColor(this, statusColor));
        
        // Hide cancel button if appointment is already cancelled or completed
        if (reservation.getStatus().equalsIgnoreCase("cancelled") || 
            reservation.getStatus().equalsIgnoreCase("completed")) {
            btnCancel.setVisibility(View.GONE);
        } else {
            btnCancel.setVisibility(View.VISIBLE);
        }
    }
    
    private void showCancellationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Appointment");
        builder.setMessage("Are you sure you want to cancel this appointment?");
        
        // Set up the input
        final EditText input = new EditText(this);
        input.setHint("Reason for cancellation");
        builder.setView(input);
        
        // Set up the buttons
        builder.setPositiveButton("Cancel Appointment", (dialog, which) -> {
            String reason = input.getText().toString().trim();
            if (reason.isEmpty()) {
                Toast.makeText(AppointmentDetailsActivity.this, "Please provide a reason for cancellation", Toast.LENGTH_SHORT).show();
                return;
            }
            
            cancelAppointment(reason);
        });
        builder.setNegativeButton("Back", (dialog, which) -> dialog.cancel());
        
        builder.show();
    }
    
    private void cancelAppointment(String reason) {
        progressBar.setVisibility(View.VISIBLE);
        btnCancel.setEnabled(false);

        apiService.cancelReservation(reservationId, reason).enqueue(new Callback<BaseResponse<ReservationResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<ReservationResponse>> call, Response<BaseResponse<ReservationResponse>> response) {
                progressBar.setVisibility(View.GONE);
                btnCancel.setEnabled(true);

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(AppointmentDetailsActivity.this, "Appointment cancelled successfully", Toast.LENGTH_LONG).show();
                    // Reload details to show updated status
                    loadReservationDetails();
                } else {
                    String errorMessage = "Failed to cancel appointment";
                    if (response.body() != null) {
                        errorMessage = response.body().getMessage();
                    }
                    Toast.makeText(AppointmentDetailsActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ReservationResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnCancel.setEnabled(true);
                Toast.makeText(AppointmentDetailsActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 