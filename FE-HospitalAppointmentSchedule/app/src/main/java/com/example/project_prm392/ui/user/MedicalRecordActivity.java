package com.example.project_prm392.ui.user;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.adapters.MedicalRecordAdapter;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.MedicalRecordResponse;
import com.example.project_prm392.network.ApiService;
import com.example.project_prm392.network.RetrofitClient;
import com.example.project_prm392.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicalRecordActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MedicalRecordAdapter adapter;
    private List<MedicalRecordResponse> medicalRecords;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record);

        // Initialize views
        recyclerView = findViewById(R.id.medicalRecordRecyclerView);

        // Initialize data
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        sessionManager = new SessionManager(this);
        medicalRecords = new ArrayList<>();

        // Setup RecyclerView
        adapter = new MedicalRecordAdapter(medicalRecords);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load medical records
        loadMedicalRecords();
    }

    private void loadMedicalRecords() {
        int patientId = sessionManager.getUserId();
        apiService.getMedicalRecords(patientId).enqueue(new Callback<BaseResponse<List<MedicalRecordResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<MedicalRecordResponse>>> call, Response<BaseResponse<List<MedicalRecordResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    medicalRecords.clear();
                    medicalRecords.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    
                    if (medicalRecords.isEmpty()) {
                        Toast.makeText(MedicalRecordActivity.this, "No medical records found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MedicalRecordActivity.this, "Failed to load medical records", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<MedicalRecordResponse>>> call, Throwable t) {
                Toast.makeText(MedicalRecordActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
} 