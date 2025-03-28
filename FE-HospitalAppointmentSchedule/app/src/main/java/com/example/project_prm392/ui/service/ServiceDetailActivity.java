package com.example.project_prm392.ui.service;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.example.project_prm392.adapter.DoctorAdapter;
import com.example.project_prm392.model.Service;
import com.example.project_prm392.ui.doctor.DoctorDetailActivity;
import com.example.project_prm392.ui.reservation.SelectDoctorActivity;
import com.example.project_prm392.utils.Constants;
import com.example.project_prm392.utils.Resource;
import com.example.project_prm392.viewmodel.ServiceViewModel;
import com.example.project_prm392.viewmodel.ViewModelFactory;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.NumberFormat;
import java.util.Locale;

public class ServiceDetailActivity extends AppCompatActivity implements DoctorAdapter.OnDoctorClickListener {
    private ImageView ivServiceImage;
    private TextView tvServiceName, tvServicePrice, tvServiceDuration, tvServiceDescription;
    private RecyclerView rvDoctors;
    private FloatingActionButton fabBookService;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    
    private ServiceViewModel serviceViewModel;
    private DoctorAdapter doctorAdapter;
    
    private int serviceId;
    private Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
        
        // Lấy serviceId từ intent
        serviceId = getIntent().getIntExtra(Constants.EXTRA_SERVICE_ID, -1);
        if (serviceId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin dịch vụ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Khởi tạo ViewModel
        serviceViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance())
                .get(ServiceViewModel.class);
        
        // Khởi tạo Views
        initViews();
        
        // Thiết lập Toolbar
        setupToolbar();
        
        // Thiết lập RecyclerView
        setupRecyclerView();
        
        // Thiết lập listeners
        setupListeners();
        
        // Load dữ liệu
        loadServiceDetails();
    }
    
    private void initViews() {
        ivServiceImage = findViewById(R.id.ivServiceImage);
        tvServiceName = findViewById(R.id.tvServiceName);
        tvServicePrice = findViewById(R.id.tvServicePrice);
        tvServiceDuration = findViewById(R.id.tvServiceDuration);
        tvServiceDescription = findViewById(R.id.tvServiceDescription);
        rvDoctors = findViewById(R.id.rvDoctors);
        fabBookService = findViewById(R.id.fabBookService);
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
    
    private void setupRecyclerView() {
        // Setup RecyclerView cho bác sĩ
        doctorAdapter = new DoctorAdapter(this, this);
        rvDoctors.setLayoutManager(new LinearLayoutManager(this));
        rvDoctors.setAdapter(doctorAdapter);
    }
    
    private void setupListeners() {
        fabBookService.setOnClickListener(v -> {
            if (service != null) {
                // Chuyển đến màn hình chọn bác sĩ
                Intent intent = new Intent(ServiceDetailActivity.this, SelectDoctorActivity.class);
                intent.putExtra(Constants.EXTRA_SERVICE_ID, serviceId);
                startActivity(intent);
            }
        });
    }
    
    private void loadServiceDetails() {
        showLoading(true);
        
        serviceViewModel.getServiceById(serviceId).observe(this, result -> {
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                service = result.data;
                updateUI(service);
                showLoading(false);
            } else if (result.status == Resource.Status.ERROR) {
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show();
                showLoading(false);
            }
        });
    }
    
    private void updateUI(Service service) {
        // Thiết lập thông tin dịch vụ
        String serviceName = service.getServiceName();
        tvServiceName.setText(serviceName);
        collapsingToolbar.setTitle(serviceName);
        
        // Format giá tiền
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvServicePrice.setText(numberFormat.format(service.getPrice()));
        
        // Thiết lập thời gian
        tvServiceDuration.setText("Thời gian: " + service.getDuration());
        
        // Thiết lập mô tả
        tvServiceDescription.setText(service.getDescription());
        
        // Load ảnh dịch vụ
        if (service.getImage() != null && !service.getImage().isEmpty()) {
            Glide.with(this)
                    .load(service.getImage())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(ivServiceImage);
        }
        
        // Thiết lập danh sách bác sĩ
        if (service.getDoctors() != null && !service.getDoctors().isEmpty()) {
            doctorAdapter.setDoctorList(service.getDoctors());
        }
    }
    
    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        if (isLoading) {
            fabBookService.hide();
        } else {
            fabBookService.show();
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
    public void onDoctorClick(com.example.project_prm392.model.Doctor doctor) {
        // Chuyển đến màn hình chi tiết bác sĩ
        Intent intent = new Intent(this, DoctorDetailActivity.class);
        intent.putExtra(Constants.EXTRA_DOCTOR_ID, doctor.getDoctorId());
        startActivity(intent);
    }
} 