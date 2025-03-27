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
import com.example.project_prm392.adapters.ServicesAdapter;
import com.example.project_prm392.adapters.SpecialtiesAdapter;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.DoctorDetailsResponse;
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
public class DoctorDetailActivity extends AppCompatActivity implements ServicesAdapter.OnServiceClickListener {

    @Inject
    ApiService apiService;

    private Toolbar toolbar;
    private ImageView imgDoctor;
    private TextView tvDoctorName;
    private TextView tvDoctorTitle;
    private TextView tvSpecialties;
    private TextView tvDoctorDescription;
    private TextView tvDoctorWorkplace;
    private RecyclerView recyclerViewSpecialties;
    private RecyclerView recyclerViewServices;
    private RecyclerView recyclerViewSchedules;
    private Button btnBookAppointment;
    private ProgressBar progressBar;

    private int doctorId;
    private DoctorDetailsResponse doctorDetails;
    private List<ServiceResponse> servicesList = new ArrayList<>();
    private ServicesAdapter servicesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);

        // Get doctor ID from intent
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
        toolbar = findViewById(R.id.toolbar);
        imgDoctor = findViewById(R.id.imgDoctorHeader);
        tvDoctorName = findViewById(R.id.tvDoctorTitle);
        tvDoctorTitle = findViewById(R.id.tvDoctorTitle);
        tvSpecialties = findViewById(R.id.tvSpecialties);
        tvDoctorDescription = findViewById(R.id.tvDoctorDescription);
        tvDoctorWorkplace = findViewById(R.id.tvWorkplace);
        recyclerViewSpecialties = findViewById(R.id.recyclerViewSpecialties);
        recyclerViewServices = findViewById(R.id.recyclerViewServices);
        recyclerViewSchedules = findViewById(R.id.recyclerViewSchedules);
        btnBookAppointment = findViewById(R.id.btnBookAppointment);
        progressBar = findViewById(R.id.progressBar);

        btnBookAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(DoctorDetailActivity.this, BookAppointmentActivity.class);
            intent.putExtra("doctorId", doctorId);
            intent.putExtra("doctorName", doctorDetails != null ? doctorDetails.getUserName() : "");
            intent.putExtra("specialty", doctorDetails != null && !doctorDetails.getSpecialties().isEmpty() 
                    ? doctorDetails.getSpecialties().get(0).getSpecialtyName() : "");
            startActivity(intent);
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Doctor Details");
        }
    }

    private void loadDoctorDetails() {
        progressBar.setVisibility(View.VISIBLE);

        apiService.getDoctorById(doctorId).enqueue(new Callback<BaseResponse<DoctorDetailsResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<DoctorDetailsResponse>> call, Response<BaseResponse<DoctorDetailsResponse>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    doctorDetails = response.body().getData();
                    displayDoctorDetails();
                    setupRecyclerViews();
                } else {
                    Toast.makeText(DoctorDetailActivity.this, "Failed to load doctor details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<DoctorDetailsResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(DoctorDetailActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayDoctorDetails() {
        if (doctorDetails == null) return;

        // Set doctor name as activity title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(doctorDetails.getUserName());
        }

        // Set doctor title/academic title
        if (doctorDetails.getAcademicTitle() != null && !doctorDetails.getAcademicTitle().isEmpty()) {
            tvDoctorTitle.setText(doctorDetails.getAcademicTitle());
            tvDoctorTitle.setVisibility(View.VISIBLE);
        } else {
            tvDoctorTitle.setVisibility(View.GONE);
        }

        // Set specialties
        StringBuilder specialtiesText = new StringBuilder();
        if (doctorDetails.getSpecialties() != null && !doctorDetails.getSpecialties().isEmpty()) {
            for (int i = 0; i < doctorDetails.getSpecialties().size(); i++) {
                specialtiesText.append(doctorDetails.getSpecialties().get(i).getSpecialtyName());
                if (i < doctorDetails.getSpecialties().size() - 1) {
                    specialtiesText.append(", ");
                }
            }
            tvSpecialties.setText(specialtiesText.toString());
        } else {
            tvSpecialties.setText("No specialties listed");
        }

        // Set workplace
        if (doctorDetails.getCurrentWork() != null && !doctorDetails.getCurrentWork().isEmpty()) {
            tvDoctorWorkplace.setText(doctorDetails.getCurrentWork());
        } else {
            tvDoctorWorkplace.setText("Information not available");
        }

        // Set description
        if (doctorDetails.getDoctorDescription() != null && !doctorDetails.getDoctorDescription().isEmpty()) {
            tvDoctorDescription.setText(doctorDetails.getDoctorDescription());
            tvDoctorDescription.setVisibility(View.VISIBLE);
        } else {
            tvDoctorDescription.setVisibility(View.GONE);
        }

        // Set doctor image
        imgDoctor.setImageResource(R.drawable.ic_doctor_avatar);
    }

    private void setupRecyclerViews() {
        // Setup services recycler view
        if (doctorDetails.getServices() != null && !doctorDetails.getServices().isEmpty()) {
            servicesList.clear();
            servicesList.addAll(doctorDetails.getServices());
            
            recyclerViewServices.setLayoutManager(new LinearLayoutManager(this));
            servicesAdapter = new ServicesAdapter(servicesList, this);
            recyclerViewServices.setAdapter(servicesAdapter);
            recyclerViewServices.setVisibility(View.VISIBLE);
        } else {
            recyclerViewServices.setVisibility(View.GONE);
        }
    }

    @Override
    public void onServiceClick(ServiceResponse service) {
        Intent intent = new Intent(this, ServiceDetailActivity.class);
        intent.putExtra("serviceId", service.getServiceId());
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 