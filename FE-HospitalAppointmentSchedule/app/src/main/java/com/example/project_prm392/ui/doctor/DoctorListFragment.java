package com.example.project_prm392.ui.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.adapter.DoctorAdapter;
import com.example.project_prm392.model.Doctor;
import com.example.project_prm392.utils.Constants;
import com.example.project_prm392.utils.Resource;
import com.example.project_prm392.viewmodel.DoctorViewModel;
import com.example.project_prm392.viewmodel.ViewModelFactory;

import java.util.ArrayList;

public class DoctorListFragment extends Fragment implements DoctorAdapter.OnDoctorClickListener {
    private RecyclerView rvDoctors;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private SearchView searchView;
    
    private DoctorViewModel doctorViewModel;
    private DoctorAdapter doctorAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_list, container, false);
        
        // Khởi tạo ViewModel
        doctorViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance())
                .get(DoctorViewModel.class);
        
        // Khởi tạo Views
        initViews(view);
        
        // Thiết lập RecyclerView
        setupRecyclerView();
        
        // Thiết lập SearchView
        setupSearchView();
        
        // Load dữ liệu
        loadDoctors();
        
        return view;
    }
    
    private void initViews(View view) {
        rvDoctors = view.findViewById(R.id.rvDoctors);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        searchView = view.findViewById(R.id.searchView);
    }
    
    private void setupRecyclerView() {
        doctorAdapter = new DoctorAdapter(requireContext(), this);
        rvDoctors.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvDoctors.setAdapter(doctorAdapter);
    }
    
    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDoctors(query);
                return true;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    loadDoctors();
                }
                return false;
            }
        });
    }
    
    private void loadDoctors() {
        showLoading(true);
        
        doctorViewModel.getAllDoctors().observe(getViewLifecycleOwner(), result -> {
            showLoading(false);
            
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                updateUI(result.data);
            } else if (result.status == Resource.Status.ERROR) {
                showError(result.message);
            }
        });
    }
    
    private void searchDoctors(String keyword) {
        showLoading(true);
        
        doctorViewModel.searchDoctors(keyword).observe(getViewLifecycleOwner(), result -> {
            showLoading(false);
            
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                updateUI(result.data);
            } else if (result.status == Resource.Status.ERROR) {
                showError(result.message);
            }
        });
    }
    
    private void updateUI(java.util.List<Doctor> doctors) {
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
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        tvEmpty.setText(R.string.error);
        tvEmpty.setVisibility(View.VISIBLE);
        rvDoctors.setVisibility(View.GONE);
    }
    
    @Override
    public void onDoctorClick(Doctor doctor) {
        // Chuyển sang màn hình chi tiết bác sĩ
        Intent intent = new Intent(requireContext(), DoctorDetailActivity.class);
        intent.putExtra(Constants.EXTRA_DOCTOR_ID, doctor.getDoctorId());
        startActivity(intent);
    }
} 