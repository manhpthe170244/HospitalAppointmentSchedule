package com.example.project_prm392.ui.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.adapters.DoctorAdapter;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.DoctorResponse;
import com.example.project_prm392.network.ApiService;
import com.example.project_prm392.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private EditText searchEditText;
    private Spinner specialtySpinner;
    private RecyclerView recyclerView;
    private DoctorAdapter adapter;
    private List<DoctorResponse> doctors;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize views
        searchEditText = findViewById(R.id.searchEditText);
        specialtySpinner = findViewById(R.id.specialtySpinner);
        recyclerView = findViewById(R.id.searchRecyclerView);

        // Initialize data
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        doctors = new ArrayList<>();

        // Setup RecyclerView
        adapter = new DoctorAdapter(doctors, this::onDoctorClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Setup search
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchDoctors(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void searchDoctors(String query) {
        apiService.searchDoctors(query).enqueue(new Callback<BaseResponse<List<DoctorResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<DoctorResponse>>> call, Response<BaseResponse<List<DoctorResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    doctors.clear();
                    doctors.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    
                    if (doctors.isEmpty()) {
                        Toast.makeText(SearchActivity.this, "No doctors found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "Failed to search doctors", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<DoctorResponse>>> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onDoctorClick(DoctorResponse doctor) {
        // TODO: Open doctor details activity
        Toast.makeText(this, "View doctor: " + doctor.getName(), Toast.LENGTH_SHORT).show();
    }
} 