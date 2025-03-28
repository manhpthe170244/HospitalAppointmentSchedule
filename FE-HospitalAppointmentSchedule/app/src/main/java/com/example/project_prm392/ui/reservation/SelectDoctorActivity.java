package com.example.project_prm392.ui.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.adapter.DoctorAdapter;
import com.example.project_prm392.model.Doctor;
import com.example.project_prm392.model.Service;
import com.example.project_prm392.utils.Constants;
import com.example.project_prm392.utils.Resource;
import com.example.project_prm392.viewmodel.DoctorViewModel;
import com.example.project_prm392.viewmodel.ServiceViewModel;
import com.example.project_prm392.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class SelectDoctorActivity extends AppCompatActivity implements DoctorAdapter.OnDoctorClickListener {
    private RecyclerView rvDoctors;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private SearchView searchView;
    private Toolbar toolbar;
    
    private ServiceViewModel serviceViewModel;
    private DoctorViewModel doctorViewModel;
    private DoctorAdapter doctorAdapter;
    
    private int serviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_doctor);
        
        // Lấy serviceId từ intent
        serviceId = getIntent().getIntExtra(Constants.EXTRA_SERVICE_ID, -1);
        if (serviceId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin dịch vụ", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Khởi tạo ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance();
        serviceViewModel = new ViewModelProvider(this, factory).get(ServiceViewModel.class);
        doctorViewModel = new ViewModelProvider(this, factory).get(DoctorViewModel.class);
        
        // Khởi tạo Views
        initViews();
        
        // Thiết lập Toolbar
        setupToolbar();
        
        // Thiết lập RecyclerView
        setupRecyclerView();
        
        // Thiết lập SearchView
        setupSearchView();
        
        // Load danh sách bác sĩ
        loadDoctors();
    }
    
    private void initViews() {
        rvDoctors = findViewById(R.id.rvDoctors);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);
        searchView = findViewById(R.id.searchView);
        toolbar = findViewById(R.id.toolbar);
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void setupRecyclerView() {
        doctorAdapter = new DoctorAdapter(this, this);
        rvDoctors.setLayoutManager(new LinearLayoutManager(this));
        rvDoctors.setAdapter(doctorAdapter);
    }
    
    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Chức năng tìm kiếm sẽ được triển khai sau
                return true;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                // Có thể thêm tính năng tìm kiếm realtime nếu cần
                return false;
            }
        });
    }
    
    private void loadDoctors() {
        showLoading(true);
        
        // Lấy danh sách bác sĩ cho dịch vụ này
        doctorViewModel.getDoctorsByService(serviceId).observe(this, result -> {
            showLoading(false);
            
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                updateUI(result.data);
            } else if (result.status == Resource.Status.ERROR) {
                showError(result.message);
            }
        });
    }
    
    private void updateUI(List<Doctor> doctors) {
        if (doctors.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvDoctors.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvDoctors.setVisibility(View.VISIBLE);
            doctorAdapter.setDoctorList(doctors);
        }
    }
    
    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        if (isLoading) {
            rvDoctors.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.GONE);
        }
    }
    
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        tvEmpty.setText(message);
        tvEmpty.setVisibility(View.VISIBLE);
        rvDoctors.setVisibility(View.GONE);
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
    public void onDoctorClick(Doctor doctor) {
        // Chuyển đến màn hình đặt lịch với bác sĩ và dịch vụ đã chọn
        Intent intent = new Intent(this, BookAppointmentActivity.class);
        intent.putExtra(Constants.EXTRA_DOCTOR_ID, doctor.getDoctorId());
        intent.putExtra(Constants.EXTRA_SERVICE_ID, serviceId);
        startActivity(intent);
    }
} 