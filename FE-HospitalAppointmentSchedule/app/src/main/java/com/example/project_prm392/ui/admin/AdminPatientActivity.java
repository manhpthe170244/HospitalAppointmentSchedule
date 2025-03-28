package com.example.project_prm392.ui.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.adapters.PatientAdapter;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.PatientResponse;
import com.example.project_prm392.network.ApiService;
import com.example.project_prm392.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPatientActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PatientAdapter adapter;
    private List<PatientResponse> patients;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_patient);

        // Initialize views
        recyclerView = findViewById(R.id.patientRecyclerView);

        // Initialize data
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        patients = new ArrayList<>();

        // Setup RecyclerView
        adapter = new PatientAdapter(patients, this::onPatientClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load patients
        loadPatients();
    }

    private void loadPatients() {
        apiService.getAllPatients().enqueue(new Callback<BaseResponse<List<PatientResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<PatientResponse>>> call, Response<BaseResponse<List<PatientResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    patients.clear();
                    patients.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    
                    if (patients.isEmpty()) {
                        Toast.makeText(AdminPatientActivity.this, "No patients found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminPatientActivity.this, "Failed to load patients", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<PatientResponse>>> call, Throwable t) {
                Toast.makeText(AdminPatientActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onPatientClick(PatientResponse patient) {
        // TODO: Open patient details dialog
        Toast.makeText(this, "View patient: " + patient.getName(), Toast.LENGTH_SHORT).show();
    }
} 