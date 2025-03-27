package com.example.project_prm392.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.project_prm392.R;
import com.example.project_prm392.adapters.ServicesAdapter;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.ServiceResponse;
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
public class ViewServicesActivity extends AppCompatActivity implements ServicesAdapter.OnServiceClickListener {

    @Inject
    ApiService apiService;

    private RecyclerView recyclerViewServices;
    private SwipeRefreshLayout swipeRefresh;
    private ProgressBar progressBar;
    private EditText etSearch;
    private Spinner specialtySpinner;
    
    private ServicesAdapter servicesAdapter;
    private List<ServiceResponse> servicesList = new ArrayList<>();
    private List<SpecialtyResponse> specialtiesList = new ArrayList<>();
    private int selectedSpecialtyId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_services);
        
        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Services");
        
        // Initialize views
        recyclerViewServices = findViewById(R.id.recyclerViewServices);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        progressBar = findViewById(R.id.progressBar);
        etSearch = findViewById(R.id.etSearch);
        specialtySpinner = findViewById(R.id.specialtySpinner);
        
        // Set up RecyclerView
        recyclerViewServices.setLayoutManager(new LinearLayoutManager(this));
        servicesAdapter = new ServicesAdapter(servicesList, this);
        recyclerViewServices.setAdapter(servicesAdapter);
        
        // Set up SwipeRefreshLayout
        swipeRefresh.setOnRefreshListener(this::loadServices);
        
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
                filterServices(s.toString());
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
                    loadServices();
                } else {
                    selectedSpecialtyId = specialtiesList.get(position - 1).getSpecialtyId();
                    loadServicesBySpecialty(selectedSpecialtyId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSpecialtyId = -1;
                loadServices();
            }
        });
    }
    
    private void loadSpecialties() {
        progressBar.setVisibility(View.VISIBLE);
        
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
                    
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            ViewServicesActivity.this,
                            android.R.layout.simple_spinner_item,
                            specialtyNames
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    specialtySpinner.setAdapter(adapter);
                    
                    // Load services after specialties are loaded
                    loadServices();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ViewServicesActivity.this, "Error loading specialties", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<BaseResponse<List<SpecialtyResponse>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ViewServicesActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void loadServices() {
        progressBar.setVisibility(View.VISIBLE);
        
        apiService.getAllServices().enqueue(new Callback<BaseResponse<List<ServiceResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ServiceResponse>>> call, Response<BaseResponse<List<ServiceResponse>>> response) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    servicesList.clear();
                    servicesList.addAll(response.body().getData());
                    servicesAdapter.notifyDataSetChanged();
                    
                    // Apply search filter if exists
                    String searchTerm = etSearch.getText().toString().trim();
                    if (!searchTerm.isEmpty()) {
                        filterServices(searchTerm);
                    }
                } else {
                    Toast.makeText(ViewServicesActivity.this, "Error loading services", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<BaseResponse<List<ServiceResponse>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                Toast.makeText(ViewServicesActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void loadServicesBySpecialty(int specialtyId) {
        progressBar.setVisibility(View.VISIBLE);
        
        apiService.getServicesBySpecialty(specialtyId).enqueue(new Callback<BaseResponse<List<ServiceResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ServiceResponse>>> call, Response<BaseResponse<List<ServiceResponse>>> response) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    servicesList.clear();
                    servicesList.addAll(response.body().getData());
                    servicesAdapter.notifyDataSetChanged();
                    
                    // Apply search filter if exists
                    String searchTerm = etSearch.getText().toString().trim();
                    if (!searchTerm.isEmpty()) {
                        filterServices(searchTerm);
                    }
                } else {
                    Toast.makeText(ViewServicesActivity.this, "Error loading services", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<BaseResponse<List<ServiceResponse>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                Toast.makeText(ViewServicesActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void filterServices(String searchTerm) {
        searchTerm = searchTerm.toLowerCase();
        
        if (searchTerm.isEmpty()) {
            servicesAdapter.setFilteredList(servicesList);
            return;
        }
        
        List<ServiceResponse> filteredList = new ArrayList<>();
        for (ServiceResponse service : servicesList) {
            if (service.getServiceName().toLowerCase().contains(searchTerm) || 
                service.getOverview().toLowerCase().contains(searchTerm)) {
                filteredList.add(service);
            }
        }
        
        servicesAdapter.setFilteredList(filteredList);
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