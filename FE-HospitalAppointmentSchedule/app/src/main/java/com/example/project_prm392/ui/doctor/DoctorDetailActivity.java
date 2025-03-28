package com.example.project_prm392.ui.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_prm392.R;
import com.example.project_prm392.adapter.DoctorScheduleAdapter;
import com.example.project_prm392.adapter.ServiceAdapter;
import com.example.project_prm392.model.Doctor;
import com.example.project_prm392.model.DoctorSchedule;
import com.example.project_prm392.model.Service;
import com.example.project_prm392.ui.reservation.BookAppointmentActivity;
import com.example.project_prm392.ui.service.ServiceDetailActivity;
import com.example.project_prm392.utils.Constants;
import com.example.project_prm392.utils.Resource;
import com.example.project_prm392.viewmodel.DoctorViewModel;
import com.example.project_prm392.viewmodel.ViewModelFactory;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DoctorDetailActivity extends AppCompatActivity implements ServiceAdapter.OnServiceClickListener {
    private ImageView ivDoctorImage;
    private TextView tvDoctorName, tvDoctorDegree, tvDoctorSpecialty, tvDoctorDescription, tvExperience;
    private RatingBar ratingBar;
    private RecyclerView rvServices, rvSchedules;
    private FloatingActionButton fabBookAppointment;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    
    private DoctorViewModel doctorViewModel;
    private ServiceAdapter serviceAdapter;
    private DoctorScheduleAdapter scheduleAdapter;
    
    private int doctorId;
    private Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_detail);
        
        // Lấy doctorId từ intent
        doctorId = getIntent().getIntExtra(Constants.EXTRA_DOCTOR_ID, -1);
        if (doctorId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin bác sĩ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Khởi tạo ViewModel
        doctorViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance())
                .get(DoctorViewModel.class);
        
        // Khởi tạo Views
        initViews();
        
        // Thiết lập Toolbar
        setupToolbar();
        
        // Thiết lập RecyclerView
        setupRecyclerViews();
        
        // Thiết lập listeners
        setupListeners();
        
        // Load dữ liệu
        loadDoctorDetails();
    }
    
    private void initViews() {
        ivDoctorImage = findViewById(R.id.ivDoctorImage);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvDoctorDegree = findViewById(R.id.tvDoctorDegree);
        tvDoctorSpecialty = findViewById(R.id.tvDoctorSpecialty);
        tvDoctorDescription = findViewById(R.id.tvDoctorDescription);
        tvExperience = findViewById(R.id.tvExperience);
        ratingBar = findViewById(R.id.ratingBar);
        rvServices = findViewById(R.id.rvServices);
        rvSchedules = findViewById(R.id.rvSchedules);
        fabBookAppointment = findViewById(R.id.fabBookAppointment);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }
    
    private void setupRecyclerViews() {
        // Setup RecyclerView cho dịch vụ
        serviceAdapter = new ServiceAdapter(this, this);
        rvServices.setLayoutManager(new LinearLayoutManager(this));
        rvServices.setAdapter(serviceAdapter);
        
        // Setup RecyclerView cho lịch làm việc
        scheduleAdapter = new DoctorScheduleAdapter(this);
        rvSchedules.setLayoutManager(new LinearLayoutManager(this));
        rvSchedules.setAdapter(scheduleAdapter);
    }
    
    private void setupListeners() {
        fabBookAppointment.setOnClickListener(v -> {
            if (doctor != null) {
                Intent intent = new Intent(DoctorDetailActivity.this, BookAppointmentActivity.class);
                intent.putExtra(Constants.EXTRA_DOCTOR_ID, doctorId);
                startActivity(intent);
            }
        });
    }
    
    private void loadDoctorDetails() {
        showLoading(true);
        
        doctorViewModel.getDoctorById(doctorId).observe(this, result -> {
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                doctor = result.data;
                updateUI(doctor);
                
                // Tải lịch làm việc của bác sĩ
                loadDoctorSchedules();
                
                showLoading(false);
            } else if (result.status == Resource.Status.ERROR) {
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show();
                showLoading(false);
            }
        });
    }
    
    private void loadDoctorSchedules() {
        doctorViewModel.getDoctorSchedules(doctorId).observe(this, result -> {
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                scheduleAdapter.setScheduleList(result.data);
            } else if (result.status == Resource.Status.ERROR) {
                Toast.makeText(this, "Lỗi khi tải lịch làm việc: " + result.message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void updateUI(Doctor doctor) {
        // Thiết lập thông tin bác sĩ
        if (doctor.getDoctorNavigation() != null) {
            String doctorName = doctor.getDoctorNavigation().getFullName();
            tvDoctorName.setText(doctorName);
            collapsingToolbar.setTitle(doctorName);
            
            // Load ảnh bác sĩ
            if (doctor.getDoctorNavigation().getImage() != null) {
                Glide.with(this)
                        .load(doctor.getDoctorNavigation().getImage())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(ivDoctorImage);
            }
        }
        
        // Thiết lập học vị và bằng cấp
        String degreeInfo = doctor.getAcademicTitle();
        if (doctor.getDegree() != null && !doctor.getDegree().isEmpty()) {
            degreeInfo += ", " + doctor.getDegree();
        }
        tvDoctorDegree.setText(degreeInfo);
        
        // Thiết lập chuyên khoa
        if (doctor.getSpecialties() != null && !doctor.getSpecialties().isEmpty()) {
            tvDoctorSpecialty.setText("Chuyên khoa " + doctor.getSpecialties().get(0).getSpecialtyName());
        } else {
            tvDoctorSpecialty.setText("Chuyên khoa");
        }
        
        // Thiết lập mô tả
        tvDoctorDescription.setText(doctor.getDoctorDescription());
        
        // Thiết lập kinh nghiệm
        tvExperience.setText(doctor.getWorkExperience());
        
        // Thiết lập rating (giả định rating 4.5)
        ratingBar.setRating(4.5f);
        
        // Thiết lập danh sách dịch vụ
        if (doctor.getServices() != null && !doctor.getServices().isEmpty()) {
            serviceAdapter.setServiceList(doctor.getServices());
        }
    }
    
    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        if (isLoading) {
            fabBookAppointment.hide();
        } else {
            fabBookAppointment.show();
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onServiceClick(Service service) {
        Intent intent = new Intent(this, ServiceDetailActivity.class);
        intent.putExtra(Constants.EXTRA_SERVICE_ID, service.getServiceId());
        startActivity(intent);
    }
    
    @Override
    public void onViewDetailsClick(Service service) {
        Intent intent = new Intent(this, ServiceDetailActivity.class);
        intent.putExtra(Constants.EXTRA_SERVICE_ID, service.getServiceId());
        startActivity(intent);
    }
} 