package com.example.project_prm392.ui.service;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.adapter.ServiceAdapter;
import com.example.project_prm392.model.Service;
import com.example.project_prm392.utils.Constants;
import com.example.project_prm392.utils.Resource;
import com.example.project_prm392.viewmodel.ServiceViewModel;
import com.example.project_prm392.viewmodel.ViewModelFactory;

public class ServiceListFragment extends Fragment implements ServiceAdapter.OnServiceClickListener {
    private RecyclerView rvServices;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private SearchView searchView;
    
    private ServiceViewModel serviceViewModel;
    private ServiceAdapter serviceAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_list, container, false);
        
        // Khởi tạo ViewModel
        serviceViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance())
                .get(ServiceViewModel.class);
        
        // Khởi tạo Views
        initViews(view);
        
        // Thiết lập RecyclerView
        setupRecyclerView();
        
        // Thiết lập SearchView
        setupSearchView();
        
        // Load dữ liệu
        loadServices();
        
        return view;
    }
    
    private void initViews(View view) {
        rvServices = view.findViewById(R.id.rvServices);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmpty = view.findViewById(R.id.tvEmpty);
        searchView = view.findViewById(R.id.searchView);
    }
    
    private void setupRecyclerView() {
        serviceAdapter = new ServiceAdapter(requireContext(), this);
        rvServices.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        rvServices.setAdapter(serviceAdapter);
    }
    
    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchServices(query);
                return true;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    loadServices();
                }
                return false;
            }
        });
    }
    
    private void loadServices() {
        showLoading(true);
        
        serviceViewModel.getAllServices().observe(getViewLifecycleOwner(), result -> {
            showLoading(false);
            
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                updateUI(result.data);
            } else if (result.status == Resource.Status.ERROR) {
                showError(result.message);
            }
        });
    }
    
    private void searchServices(String keyword) {
        showLoading(true);
        
        serviceViewModel.searchServices(keyword).observe(getViewLifecycleOwner(), result -> {
            showLoading(false);
            
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                updateUI(result.data);
            } else if (result.status == Resource.Status.ERROR) {
                showError(result.message);
            }
        });
    }
    
    private void updateUI(java.util.List<Service> services) {
        if (services.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvServices.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvServices.setVisibility(View.VISIBLE);
            serviceAdapter.setServiceList(services);
        }
    }
    
    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        if (isLoading) {
            rvServices.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.GONE);
        }
    }
    
    private void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        tvEmpty.setText(R.string.error);
        tvEmpty.setVisibility(View.VISIBLE);
        rvServices.setVisibility(View.GONE);
    }
    
    @Override
    public void onServiceClick(Service service) {
        // Chuyển sang màn hình chi tiết dịch vụ
        Intent intent = new Intent(requireContext(), ServiceDetailActivity.class);
        intent.putExtra(Constants.EXTRA_SERVICE_ID, service.getServiceId());
        startActivity(intent);
    }
    
    @Override
    public void onViewDetailsClick(Service service) {
        // Chuyển sang màn hình chi tiết dịch vụ
        Intent intent = new Intent(requireContext(), ServiceDetailActivity.class);
        intent.putExtra(Constants.EXTRA_SERVICE_ID, service.getServiceId());
        startActivity(intent);
    }
} 