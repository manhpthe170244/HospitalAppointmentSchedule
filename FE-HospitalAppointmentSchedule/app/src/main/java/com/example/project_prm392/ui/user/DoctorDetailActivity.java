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
import com.example.project_prm392.models.responses.CertificationResponse;
import com.example.project_prm392.models.responses.DoctorDetailsResponse;
import com.example.project_prm392.models.responses.DoctorScheduleResponse;
import com.example.project_prm392.models.responses.ServiceResponse;
import com.example.project_prm392.repository.DoctorRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DoctorDetailActivity extends AppCompatActivity implements ServicesAdapter.OnServiceClickListener, SchedulesAdapter.OnScheduleClickListener {

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

    private final List<ServiceResponse> servicesList = new ArrayList<>();
    private final List<DoctorScheduleResponse> schedulesList = new ArrayList<>();
    private final List<CertificationResponse> certificationsList = new ArrayList<>();

    private ServicesAdapter servicesAdapter;
    private SchedulesAdapter schedulesAdapter;
    private CertificationsAdapter certificationsAdapter;

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
        setupRecyclerViews();
        loadDoctorDetails();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvSpecialty = findViewById(R.id.tvSpecialty);
        tvExperience = findViewById(R.id.tvExperience);
        tvDescription = findViewById(R.id.tvDoctorDescription);
        recyclerViewServices = findViewById(R.id.recyclerViewServices);
        recyclerViewSchedules = findViewById(R.id.recyclerViewSchedules);
        recyclerViewCertifications = findViewById(R.id.recyclerViewCertifications);
        btnBookAppointment = findViewById(R.id.btnBookAppointment);

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

    private void setupRecyclerViews() {
        // Setup Services RecyclerView
        recyclerViewServices.setLayoutManager(new LinearLayoutManager(this));
        servicesAdapter = new ServicesAdapter(servicesList, this);
        recyclerViewServices.setAdapter(servicesAdapter);

        // Setup Schedules RecyclerView
        recyclerViewSchedules.setLayoutManager(new LinearLayoutManager(this));
        schedulesAdapter = new SchedulesAdapter(schedulesList, this);
        recyclerViewSchedules.setAdapter(schedulesAdapter);

        // Setup Certifications RecyclerView
        recyclerViewCertifications.setLayoutManager(new LinearLayoutManager(this));
        certificationsAdapter = new CertificationsAdapter(certificationsList);
        recyclerViewCertifications.setAdapter(certificationsAdapter);
    }

    private void loadDoctorDetails() {
        progressBar.setVisibility(View.VISIBLE);
        
        doctorRepository.getDoctorById(doctorId).observe(this, response -> {
            if (response != null && response.isSuccess()) {
                DoctorDetailsResponse doctor = response.getData();
                displayDoctorDetails(doctor);
            } else {
                String message = response != null ? response.getMessage() : "Failed to load doctor details";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });
        
        loadDoctorSchedules();
    }

    private void displayDoctorDetails(DoctorDetailsResponse doctor) {
        tvDoctorName.setText(doctor.getUserName());
        tvSpecialty.setText(doctor.getSpecialtyName());
        tvExperience.setText(String.format("%d years of experience", doctor.getYearsOfExperience()));
        tvDescription.setText(doctor.getDescription());

        // Update services list
        servicesList.clear();
        if (doctor.getServices() != null) {
            servicesList.addAll(doctor.getServices());
        }
        servicesAdapter.notifyDataSetChanged();

        // Update certifications list
        certificationsList.clear();
        if (doctor.getCertifications() != null) {
            certificationsList.addAll(doctor.getCertifications());
        }
        certificationsAdapter.notifyDataSetChanged();
    }

    private void loadDoctorSchedules() {
        progressBar.setVisibility(View.VISIBLE);
        
        doctorRepository.getDoctorSchedules(doctorId).observe(this, response -> {
            if (response != null && response.isSuccess()) {
                schedulesList.clear();
                if (response.getData() != null) {
                    schedulesList.addAll(response.getData());
                }
                schedulesAdapter.notifyDataSetChanged();
            } else {
                String message = response != null ? response.getMessage() : "Failed to load schedules";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onServiceClick(int serviceId) {
        navigateToBookAppointment(serviceId);
    }

    @Override
    public void onScheduleClick(DoctorScheduleResponse schedule) {
        navigateToBookAppointment(schedule.getServiceId());
    }

    private void navigateToBookAppointment(int serviceId) {
        Intent intent = new Intent(this, BookAppointmentActivity.class);
        intent.putExtra("doctorId", doctorId);
        intent.putExtra("serviceId", serviceId);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
} 