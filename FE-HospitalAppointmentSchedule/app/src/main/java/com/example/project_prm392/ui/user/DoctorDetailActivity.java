package com.example.project_prm392.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.adapters.CertificationsAdapter;
import com.example.project_prm392.adapters.SchedulesAdapter;
import com.example.project_prm392.adapters.ServicesAdapter;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.DoctorDetailsResponse;
import com.example.project_prm392.repository.DoctorRepository;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class DoctorDetailActivity extends AppCompatActivity implements ServicesAdapter.OnServiceClickListener {

    @Inject
    DoctorRepository doctorRepository;

    private int doctorId;
    private ProgressBar progressBar;
    private TextView tvDoctorName;
    private TextView tvSpecialty;
    private TextView tvExperience;
    private TextView tvDescription;
    private RecyclerView recyclerViewServices;
    private RecyclerView recyclerViewSchedules;
    private RecyclerView recyclerViewCertifications;
    private Button btnBookAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);

        doctorId = getIntent().getIntExtra("doctorId", -1);
        if (doctorId == -1) {
            Toast.makeText(this, "Invalid doctor ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupToolbar();
        loadDoctorDetails();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvSpecialty = findViewById(R.id.tvSpecialty);
        tvExperience = findViewById(R.id.tvExperience);
        tvDescription = findViewById(R.id.tvDescription);
        recyclerViewServices = findViewById(R.id.recyclerViewServices);
        recyclerViewSchedules = findViewById(R.id.recyclerViewSchedules);
        recyclerViewCertifications = findViewById(R.id.recyclerViewCertifications);
        btnBookAppointment = findViewById(R.id.btnBookAppointment);

        recyclerViewServices.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSchedules.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCertifications.setLayoutManager(new LinearLayoutManager(this));

        btnBookAppointment.setOnClickListener(v -> navigateToBookAppointment());
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Doctor Details");
        }
    }

    private void loadDoctorDetails() {
        progressBar.setVisibility(View.VISIBLE);

        doctorRepository.getDoctorById(doctorId).enqueue(new Callback<BaseResponse<DoctorDetailsResponse>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse<DoctorDetailsResponse>> call, @NonNull Response<BaseResponse<DoctorDetailsResponse>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    DoctorDetailsResponse doctor = response.body().getData();
                    displayDoctorDetails(doctor);
                } else {
                    Toast.makeText(DoctorDetailActivity.this, "Failed to load doctor details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse<DoctorDetailsResponse>> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DoctorDetailActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayDoctorDetails(DoctorDetailsResponse doctor) {
        tvDoctorName.setText(doctor.getUserName());
        if (doctor.getSpecialties() != null && !doctor.getSpecialties().isEmpty()) {
            tvSpecialty.setText(doctor.getSpecialties().get(0).getSpecialtyName());
        }
        tvExperience.setText(String.format("%d years of experience", doctor.getExperience()));
        tvDescription.setText(doctor.getDoctorDescription());

        // Set up services adapter
        ServicesAdapter servicesAdapter = new ServicesAdapter(doctor.getServices(), this);
        recyclerViewServices.setAdapter(servicesAdapter);

        // Set up schedules adapter
        SchedulesAdapter schedulesAdapter = new SchedulesAdapter(doctor.getSchedules());
        recyclerViewSchedules.setAdapter(schedulesAdapter);

        // Set up certifications adapter
        CertificationsAdapter certificationsAdapter = new CertificationsAdapter(doctor.getCertifications());
        recyclerViewCertifications.setAdapter(certificationsAdapter);
    }

    private void navigateToBookAppointment() {
        Intent intent = new Intent(this, BookAppointmentActivity.class);
        intent.putExtra("doctorId", doctorId);
        intent.putExtra("doctorName", tvDoctorName.getText().toString());
        intent.putExtra("specialty", tvSpecialty.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onServiceClick(int serviceId) {
        // Handle service click - maybe show service details or pre-select service in booking
        Intent intent = new Intent(this, BookAppointmentActivity.class);
        intent.putExtra("doctorId", doctorId);
        intent.putExtra("serviceId", serviceId);
        intent.putExtra("doctorName", tvDoctorName.getText().toString());
        intent.putExtra("specialty", tvSpecialty.getText().toString());
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 