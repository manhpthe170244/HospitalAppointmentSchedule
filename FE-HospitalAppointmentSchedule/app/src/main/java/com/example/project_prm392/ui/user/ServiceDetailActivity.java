package com.example.project_prm392.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.adapters.DoctorsAdapter;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.DoctorResponse;
import com.example.project_prm392.models.responses.ServiceResponse;
import com.example.project_prm392.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ServiceDetailActivity extends AppCompatActivity implements DoctorsAdapter.OnDoctorClickListener {

    @Inject
    ApiService apiService;

    private Toolbar toolbar;
    private ImageView imgDoctor;
    private TextView tvServiceName;
    private TextView tvSpecialty;
    private TextView tvPrice;
    private TextView tvOverview;
    private RecyclerView recyclerViewDoctors;
    private Button btnViewDoctors;
    private ProgressBar progressBar;

    private int serviceId;
    private List<DoctorResponse> doctorsList = new ArrayList<>();
    private DoctorsAdapter doctorsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);

        // Get service ID from intent
        serviceId = getIntent().getIntExtra("serviceId", -1);
        if (serviceId == -1) {
            Toast.makeText(this, "Invalid service ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupToolbar();
        setupRecyclerView();
        loadServiceDetails();
        loadDoctorsByService();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        imgDoctor = findViewById(R.id.imgDoctor);
        tvServiceName = findViewById(R.id.tvServiceName);
        tvSpecialty = findViewById(R.id.tvSpecialty);
        tvPrice = findViewById(R.id.tvPrice);
        tvOverview = findViewById(R.id.tvDoctorDescription);
        recyclerViewDoctors = findViewById(R.id.recyclerViewSpecialties);
        btnViewDoctors = findViewById(R.id.btnBookAppointment);
        progressBar = findViewById(R.id.progressBar);

        btnViewDoctors.setOnClickListener(v -> {
            Intent intent = new Intent(ServiceDetailActivity.this, ViewDoctorsActivity.class);
            intent.putExtra("serviceId", serviceId);
            startActivity(intent);
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Service Details");
        }
    }

    private void setupRecyclerView() {
        recyclerViewDoctors.setLayoutManager(new LinearLayoutManager(this));
        doctorsAdapter = new DoctorsAdapter(doctorsList, this);
        recyclerViewDoctors.setAdapter(doctorsAdapter);
    }

    private void loadServiceDetails() {
        progressBar.setVisibility(View.VISIBLE);

        apiService.getServiceById(serviceId).enqueue(new Callback<BaseResponse<ServiceResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<ServiceResponse>> call, Response<BaseResponse<ServiceResponse>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    ServiceResponse service = response.body().getData();
                    displayServiceDetails(service);
                } else {
                    Toast.makeText(ServiceDetailActivity.this, "Failed to load service details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<ServiceResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ServiceDetailActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDoctorsByService() {
        progressBar.setVisibility(View.VISIBLE);

        apiService.getDoctorsByService(serviceId).enqueue(new Callback<BaseResponse<List<DoctorResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<DoctorResponse>>> call, Response<BaseResponse<List<DoctorResponse>>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    doctorsList.clear();
                    doctorsList.addAll(response.body().getData());
                    doctorsAdapter.notifyDataSetChanged();

                    if (doctorsList.isEmpty()) {
                        recyclerViewDoctors.setVisibility(View.GONE);
                    } else {
                        recyclerViewDoctors.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(ServiceDetailActivity.this, "Failed to load doctors", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<DoctorResponse>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ServiceDetailActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayServiceDetails(ServiceResponse service) {
        tvServiceName.setText(service.getServiceName());
        tvSpecialty.setText(service.getSpecialtyName());
        tvPrice.setText(String.format("$%.2f", service.getPrice()));
        
        if (service.getOverview() != null && !service.getOverview().isEmpty()) {
            tvOverview.setText(service.getOverview());
            tvOverview.setVisibility(View.VISIBLE);
        } else {
            tvOverview.setVisibility(View.GONE);
        }
        
        // Set default service image
        imgDoctor.setImageResource(R.drawable.ic_service);
    }

    @Override
    public void onDoctorClick(int doctorId) {
        Intent intent = new Intent(this, DoctorDetailActivity.class);
        intent.putExtra("doctorId", doctorId);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 