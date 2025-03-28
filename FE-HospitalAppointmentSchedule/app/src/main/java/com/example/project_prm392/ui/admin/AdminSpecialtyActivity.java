package com.example.project_prm392.ui.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.adapters.SpecialtyAdapter;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.SpecialtyResponse;
import com.example.project_prm392.network.ApiService;
import com.example.project_prm392.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminSpecialtyActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SpecialtyAdapter adapter;
    private List<SpecialtyResponse> specialties;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_specialty);

        // Initialize views
        recyclerView = findViewById(R.id.specialtyRecyclerView);

        // Initialize data
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        specialties = new ArrayList<>();

        // Setup RecyclerView
        adapter = new SpecialtyAdapter(specialties, this::onSpecialtyClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load specialties
        loadSpecialties();
    }

    private void loadSpecialties() {
        apiService.getAllSpecialties().enqueue(new Callback<BaseResponse<List<SpecialtyResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<SpecialtyResponse>>> call, Response<BaseResponse<List<SpecialtyResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    specialties.clear();
                    specialties.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    
                    if (specialties.isEmpty()) {
                        Toast.makeText(AdminSpecialtyActivity.this, "No specialties found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminSpecialtyActivity.this, "Failed to load specialties", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<SpecialtyResponse>>> call, Throwable t) {
                Toast.makeText(AdminSpecialtyActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onSpecialtyClick(SpecialtyResponse specialty) {
        // TODO: Open specialty edit dialog
        Toast.makeText(this, "Edit specialty: " + specialty.getName(), Toast.LENGTH_SHORT).show();
    }
} 