package com.example.project_prm392.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.project_prm392.R;
import com.example.project_prm392.adapters.AdminDoctorAdapter;
import com.example.project_prm392.databinding.ActivityAdminDoctorListBinding;
import com.example.project_prm392.models.responses.DoctorResponse;
import com.example.project_prm392.viewmodels.DoctorViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AdminDoctorListActivity extends AppCompatActivity implements AdminDoctorAdapter.OnDoctorClickListener {
    private ActivityAdminDoctorListBinding binding;
    private DoctorViewModel viewModel;
    private AdminDoctorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDoctorListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupToolbar();
        setupRecyclerView();
        setupViewModel();
        setupSwipeRefresh();
        setupFab();

        loadDoctors();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        adapter = new AdminDoctorAdapter(this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(DoctorViewModel.class);
    }

    private void setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener(this::loadDoctors);
    }

    private void setupFab() {
        binding.fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminDoctorFormActivity.class);
            startActivity(intent);
        });
    }

    private void loadDoctors() {
        setLoading(true);
        viewModel.getAllDoctors().observe(this, response -> {
            setLoading(false);
            if (response.isSuccess() && response.getData() != null) {
                adapter.submitList(response.getData());
                updateEmptyView(response.getData().isEmpty());
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoading(boolean isLoading) {
        binding.swipeRefreshLayout.setRefreshing(isLoading);
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        binding.fabAdd.setEnabled(!isLoading);
    }

    private void updateEmptyView(boolean isEmpty) {
        binding.recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        binding.emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDoctorClick(DoctorResponse doctor) {
        viewModel.setSelectedDoctor(doctor);
        Intent intent = new Intent(this, AdminDoctorDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onEditClick(DoctorResponse doctor) {
        viewModel.setSelectedDoctor(doctor);
        Intent intent = new Intent(this, AdminDoctorFormActivity.class);
        intent.putExtra("doctorId", doctor.getDoctorId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(DoctorResponse doctor) {
        new AlertDialog.Builder(this)
            .setTitle(R.string.delete_doctor)
            .setMessage(getString(R.string.delete_doctor_confirmation, doctor.getUserName()))
            .setPositiveButton(R.string.delete, (dialog, which) -> deleteDoctor(doctor))
            .setNegativeButton(R.string.cancel, null)
            .show();
    }

    @Override
    public void onScheduleClick(DoctorResponse doctor) {
        viewModel.setSelectedDoctor(doctor);
        Intent intent = new Intent(this, AdminDoctorScheduleActivity.class);
        intent.putExtra("doctorId", doctor.getDoctorId());
        startActivity(intent);
    }

    private void deleteDoctor(DoctorResponse doctor) {
        setLoading(true);
        viewModel.deleteDoctor(doctor.getDoctorId()).observe(this, response -> {
            setLoading(false);
            if (response.isSuccess()) {
                Toast.makeText(this, R.string.doctor_deleted, Toast.LENGTH_SHORT).show();
                loadDoctors();
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDoctors();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
} 