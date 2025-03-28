package com.example.project_prm392.ui.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.adapters.ServiceAdapter;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.ServiceResponse;
import com.example.project_prm392.network.ApiService;
import com.example.project_prm392.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminServiceActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ServiceAdapter adapter;
    private List<ServiceResponse> services;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_service);

        // Initialize views
        recyclerView = findViewById(R.id.serviceRecyclerView);

        // Initialize data
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        services = new ArrayList<>();

        // Setup RecyclerView
        adapter = new ServiceAdapter(services, this::onServiceClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load services
        loadServices();
    }

    private void loadServices() {
        apiService.getAllServices().enqueue(new Callback<BaseResponse<List<ServiceResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ServiceResponse>>> call, Response<BaseResponse<List<ServiceResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    services.clear();
                    services.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    
                    if (services.isEmpty()) {
                        Toast.makeText(AdminServiceActivity.this, "No services found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminServiceActivity.this, "Failed to load services", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<ServiceResponse>>> call, Throwable t) {
                Toast.makeText(AdminServiceActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onServiceClick(ServiceResponse service) {
        // TODO: Open service edit dialog
        Toast.makeText(this, "Edit service: " + service.getName(), Toast.LENGTH_SHORT).show();
    }
} 