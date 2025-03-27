package com.example.project_prm392.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.project_prm392.R;
import com.example.project_prm392.adapters.ReservationsAdapter;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.ReservationResponse;
import com.example.project_prm392.network.ApiService;
import com.example.project_prm392.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class MyAppointmentsActivity extends AppCompatActivity implements ReservationsAdapter.OnReservationClickListener {

    @Inject
    ApiService apiService;
    
    @Inject
    SessionManager sessionManager;
    
    private RecyclerView recyclerViewReservations;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private TextView tvEmptyState;
    
    private ReservationsAdapter reservationsAdapter;
    private List<ReservationResponse> reservationsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointments);
        
        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Appointments");
        
        // Initialize views
        recyclerViewReservations = findViewById(R.id.recyclerViewReservations);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        progressBar = findViewById(R.id.progressBar);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        
        // Set up RecyclerView
        recyclerViewReservations.setLayoutManager(new LinearLayoutManager(this));
        reservationsAdapter = new ReservationsAdapter(reservationsList, this);
        recyclerViewReservations.setAdapter(reservationsAdapter);
        
        // Set up SwipeRefreshLayout
        swipeRefresh.setOnRefreshListener(this::loadReservations);
        
        // Load reservations
        loadReservations();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadReservations(); // Refresh data when returning to this screen
    }
    
    private void loadReservations() {
        progressBar.setVisibility(View.VISIBLE);
        tvEmptyState.setVisibility(View.GONE);
        
        int patientId = sessionManager.getUserId();
        
        apiService.getReservationsByPatient(patientId).enqueue(new Callback<BaseResponse<List<ReservationResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ReservationResponse>>> call, Response<BaseResponse<List<ReservationResponse>>> response) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    reservationsList.clear();
                    
                    List<ReservationResponse> data = response.body().getData();
                    if (data != null && !data.isEmpty()) {
                        reservationsList.addAll(data);
                        tvEmptyState.setVisibility(View.GONE);
                    } else {
                        tvEmptyState.setVisibility(View.VISIBLE);
                    }
                    
                    reservationsAdapter.notifyDataSetChanged();
                } else {
                    tvEmptyState.setVisibility(View.VISIBLE);
                    Toast.makeText(MyAppointmentsActivity.this, "Error loading appointments", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<BaseResponse<List<ReservationResponse>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                tvEmptyState.setVisibility(View.VISIBLE);
                Toast.makeText(MyAppointmentsActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    public void onReservationClick(int reservationId) {
        Intent intent = new Intent(this, AppointmentDetailsActivity.class);
        intent.putExtra("reservationId", reservationId);
        startActivity(intent);
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 