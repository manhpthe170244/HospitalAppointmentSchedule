package com.example.project_prm392.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.project_prm392.R;
import com.example.project_prm392.adapters.DoctorsAdapter;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.DoctorResponse;
import com.example.project_prm392.models.responses.SpecialtyResponse;
import com.example.project_prm392.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ViewDoctorsActivity extends AppCompatActivity implements DoctorsAdapter.OnDoctorClickListener {

    @Inject
    ApiService apiService;

    private final List<DoctorResponse> doctorsList = new ArrayList<>();
    private final List<SpecialtyResponse> specialtiesList = new ArrayList<>();
    
    private RecyclerView recyclerViewDoctors;
    private SwipeRefreshLayout swipeRefresh;
    private DoctorsAdapter doctorsAdapter;
    private EditText etSearch;
    private Spinner specialtySpinner;
    private int selectedSpecialtyId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doctors);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Doctors");

        // Initialize views
        recyclerViewDoctors = findViewById(R.id.recyclerViewDoctors);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        etSearch = findViewById(R.id.etSearch);
        specialtySpinner = findViewById(R.id.specialtySpinner);

        // Set up RecyclerView
        recyclerViewDoctors.setLayoutManager(new LinearLayoutManager(this));
        doctorsAdapter = new DoctorsAdapter(doctorsList, this);
        recyclerViewDoctors.setAdapter(doctorsAdapter);

        // Set up SwipeRefreshLayout
        swipeRefresh.setOnRefreshListener(this::loadDoctors);

        // Set up search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterDoctors(s.toString());
            }
        });

        // Load specialties for spinner
        loadSpecialties();

        // Set up specialty spinner listener
        specialtySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedSpecialtyId = -1;
                    loadDoctors();
                } else {
                    selectedSpecialtyId = specialtiesList.get(position - 1).getSpecialtyId();
                    loadDoctorsBySpecialty(selectedSpecialtyId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSpecialtyId = -1;
                loadDoctors();
            }
        });
    }

    private void loadSpecialties() {
        apiService.getAllSpecialties().enqueue(new Callback<BaseResponse<List<SpecialtyResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<SpecialtyResponse>>> call, Response<BaseResponse<List<SpecialtyResponse>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    specialtiesList.clear();
                    specialtiesList.addAll(response.body().getData());

                    // Create spinner options
                    List<String> specialtyNames = new ArrayList<>();
                    specialtyNames.add("All Specialties");
                    for (SpecialtyResponse specialty : specialtiesList) {
                        specialtyNames.add(specialty.getSpecialtyName());
                    }

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                            ViewDoctorsActivity.this,
                            android.R.layout.simple_spinner_item,
                            specialtyNames
                    );
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    specialtySpinner.setAdapter(spinnerAdapter);
                    
                    // Load all doctors after specialties are loaded
                    loadDoctors();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<SpecialtyResponse>>> call, Throwable t) {
                Toast.makeText(ViewDoctorsActivity.this, "Error loading specialties: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDoctors() {
        swipeRefresh.setRefreshing(true);
        
        apiService.getAllDoctors().enqueue(new Callback<BaseResponse<List<DoctorResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<DoctorResponse>>> call, Response<BaseResponse<List<DoctorResponse>>> response) {
                swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    doctorsList.clear();
                    doctorsList.addAll(response.body().getData());
                    
                    // Apply search filter if exists
                    String searchTerm = etSearch.getText().toString().trim();
                    if (!searchTerm.isEmpty()) {
                        filterDoctors(searchTerm);
                    } else {
                        doctorsAdapter.setFilteredList(doctorsList);
                    }
                } else {
                    Toast.makeText(ViewDoctorsActivity.this, "Error loading doctors", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<DoctorResponse>>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(ViewDoctorsActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void loadDoctorsBySpecialty(int specialtyId) {
        swipeRefresh.setRefreshing(true);
        
        apiService.getDoctorsBySpecialty(specialtyId).enqueue(new Callback<BaseResponse<List<DoctorResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<DoctorResponse>>> call, Response<BaseResponse<List<DoctorResponse>>> response) {
                swipeRefresh.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    doctorsList.clear();
                    doctorsList.addAll(response.body().getData());
                    
                    // Apply search filter if exists
                    String searchTerm = etSearch.getText().toString().trim();
                    if (!searchTerm.isEmpty()) {
                        filterDoctors(searchTerm);
                    } else {
                        doctorsAdapter.setFilteredList(doctorsList);
                    }
                } else {
                    Toast.makeText(ViewDoctorsActivity.this, "Error loading doctors", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<DoctorResponse>>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Toast.makeText(ViewDoctorsActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void filterDoctors(String searchTerm) {
        searchTerm = searchTerm.toLowerCase();
        
        if (searchTerm.isEmpty()) {
            doctorsAdapter.setFilteredList(doctorsList);
            return;
        }
        
        List<DoctorResponse> filteredList = new ArrayList<>();
        for (DoctorResponse doctor : doctorsList) {
            if (doctor.getUserName().toLowerCase().contains(searchTerm) || 
                (doctor.getSpecialtyName() != null && doctor.getSpecialtyName().toLowerCase().contains(searchTerm)) ||
                (doctor.getDoctorDescription() != null && doctor.getDoctorDescription().toLowerCase().contains(searchTerm))) {
                filteredList.add(doctor);
            }
        }
        
        doctorsAdapter.setFilteredList(filteredList);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDoctorClick(int doctorId) {
        Intent intent = new Intent(this, DoctorDetailActivity.class);
        intent.putExtra("doctorId", doctorId);
        startActivity(intent);
    }
} 