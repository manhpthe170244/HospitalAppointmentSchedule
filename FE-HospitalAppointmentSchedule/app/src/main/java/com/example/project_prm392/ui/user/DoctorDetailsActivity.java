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
import com.example.project_prm392.adapters.CertificationsAdapter;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.CertificationResponse;
import com.example.project_prm392.models.responses.DoctorDetailsResponse;
import com.example.project_prm392.models.responses.DoctorScheduleResponse;
import com.example.project_prm392.models.responses.SpecialtyResponse;
import com.example.project_prm392.network.ApiService;
import com.example.project_prm392.repository.DoctorRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class DoctorDetailsActivity extends AppCompatActivity {

    @Inject
    DoctorRepository doctorRepository;

    private int doctorId;
    private ProgressBar progressBar;
    private TextView tvDoctorName, tvSpecialty, tvDescription, tvWorkExperience, tvTrainingProcess;
    private Button btnBookAppointment;
    private RecyclerView recyclerViewCertifications;
    private CertificationsAdapter certificationsAdapter;
    private List<CertificationResponse> certificationsList = new ArrayList<>();
    private DoctorDetailsResponse doctorDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        // Get doctor ID from intent
        doctorId = getIntent().getIntExtra("doctorId", -1);
        if (doctorId == -1) {
            Toast.makeText(this, "Invalid doctor ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Doctor Profile");
        }

        // Initialize views
        progressBar = findViewById(R.id.progressBar);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvSpecialty = findViewById(R.id.tvSpecialty);
        tvDescription = findViewById(R.id.tvDescription);
        tvWorkExperience = findViewById(R.id.tvWorkExperience);
        tvTrainingProcess = findViewById(R.id.tvTrainingProcess);
        btnBookAppointment = findViewById(R.id.btnBookAppointment);
        recyclerViewCertifications = findViewById(R.id.recyclerViewCertifications);

        // Set up RecyclerView
        recyclerViewCertifications.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        certificationsAdapter = new CertificationsAdapter(certificationsList);
        recyclerViewCertifications.setAdapter(certificationsAdapter);

        // Set up book appointment button
        btnBookAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorDetailsActivity.this, BookAppointmentActivity.class);
            intent.putExtra("doctorId", doctorId);
            if (doctorDetails != null) {
                intent.putExtra("doctorName", doctorDetails.getUserName());
                if (doctorDetails.getSpecialties() != null && !doctorDetails.getSpecialties().isEmpty()) {
                    intent.putExtra("specialty", doctorDetails.getSpecialties().get(0).getSpecialtyName());
                }
            }
            startActivity(intent);
        });

        // Load doctor details
        loadDoctorDetails();
    }

    private void loadDoctorDetails() {
        progressBar.setVisibility(View.VISIBLE);

        doctorRepository.getDoctorById(doctorId).enqueue(new Callback<BaseResponse<DoctorDetailsResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<DoctorDetailsResponse>> call, Response<BaseResponse<DoctorDetailsResponse>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    doctorDetails = response.body().getData();
                    displayDoctorDetails(doctorDetails);
                } else {
                    Toast.makeText(DoctorDetailsActivity.this, "Error loading doctor details", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<DoctorDetailsResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DoctorDetailsActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void displayDoctorDetails(DoctorDetailsResponse doctor) {
        tvDoctorName.setText(doctor.getUserName());
        
        // Set specialty
        if (doctor.getSpecialties() != null && !doctor.getSpecialties().isEmpty()) {
            SpecialtyResponse specialty = doctor.getSpecialties().get(0);
            tvSpecialty.setText(specialty.getSpecialtyName());
        }

        // Set description
        String description = doctor.getDescription();
        if (description != null && !description.isEmpty()) {
            tvDescription.setText(description);
            findViewById(R.id.layoutDescription).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.layoutDescription).setVisibility(View.GONE);
        }

        // Set work experience
        String workExperience = doctor.getWorkExperience();
        if (workExperience != null && !workExperience.isEmpty()) {
            tvWorkExperience.setText(workExperience);
            findViewById(R.id.layoutWorkExperience).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.layoutWorkExperience).setVisibility(View.GONE);
        }

        // Set training process
        String trainingProcess = doctor.getTrainingProcess();
        if (trainingProcess != null && !trainingProcess.isEmpty()) {
            tvTrainingProcess.setText(trainingProcess);
            findViewById(R.id.layoutTrainingProcess).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.layoutTrainingProcess).setVisibility(View.GONE);
        }

        // Set certifications
        if (doctor.getCertifications() != null && !doctor.getCertifications().isEmpty()) {
            certificationsList.clear();
            certificationsList.addAll(doctor.getCertifications());
            certificationsAdapter.notifyDataSetChanged();
            findViewById(R.id.layoutCertifications).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.layoutCertifications).setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 