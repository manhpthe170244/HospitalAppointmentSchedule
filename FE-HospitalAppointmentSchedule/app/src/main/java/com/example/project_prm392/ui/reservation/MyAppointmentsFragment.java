package com.example.project_prm392.ui.reservation;

import android.app.AlertDialog;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.adapter.AppointmentAdapter;
import com.example.project_prm392.model.Reservation;
import com.example.project_prm392.utils.Constants;
import com.example.project_prm392.utils.Resource;
import com.example.project_prm392.viewmodel.ReservationViewModel;
import com.example.project_prm392.viewmodel.ViewModelFactory;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyAppointmentsFragment extends Fragment implements AppointmentAdapter.OnAppointmentActionListener {
    private TabLayout tabLayout;
    private RecyclerView rvAppointments;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    
    private ReservationViewModel reservationViewModel;
    private AppointmentAdapter appointmentAdapter;
    private List<Reservation> allAppointments = new ArrayList<>();
    
    private static final int TAB_UPCOMING = 0;
    private static final int TAB_COMPLETED = 1;
    private static final int TAB_CANCELLED = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointments, container, false);
        
        // Khởi tạo ViewModel
        reservationViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance())
                .get(ReservationViewModel.class);
        
        // Khởi tạo Views
        initViews(view);
        
        // Thiết lập RecyclerView
        setupRecyclerView();
        
        // Thiết lập TabLayout
        setupTabLayout();
        
        // Load dữ liệu
        loadAppointments();
        
        return view;
    }
    
    private void initViews(View view) {
        tabLayout = view.findViewById(R.id.tabLayout);
        rvAppointments = view.findViewById(R.id.rvAppointments);
        progressBar = view.findViewById(R.id.progressBar);
        tvEmpty = view.findViewById(R.id.tvEmpty);
    }
    
    private void setupRecyclerView() {
        appointmentAdapter = new AppointmentAdapter(requireContext(), this);
        rvAppointments.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvAppointments.setAdapter(appointmentAdapter);
    }
    
    private void setupTabLayout() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterAppointmentsByTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Không cần xử lý
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Không cần xử lý
            }
        });
    }
    
    private void loadAppointments() {
        showLoading(true);
        
        reservationViewModel.getReservationsByPatient().observe(getViewLifecycleOwner(), result -> {
            showLoading(false);
            
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                allAppointments = result.data;
                filterAppointmentsByTab(tabLayout.getSelectedTabPosition());
            } else if (result.status == Resource.Status.ERROR) {
                showError(result.message);
            }
        });
    }
    
    private void filterAppointmentsByTab(int tabPosition) {
        if (allAppointments.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvAppointments.setVisibility(View.GONE);
            return;
        }
        
        List<Reservation> filteredList;
        
        switch (tabPosition) {
            case TAB_UPCOMING:
                filteredList = allAppointments.stream()
                        .filter(appointment -> "Pending".equalsIgnoreCase(appointment.getStatus()) || 
                                               "Confirmed".equalsIgnoreCase(appointment.getStatus()))
                        .collect(Collectors.toList());
                break;
                
            case TAB_COMPLETED:
                filteredList = allAppointments.stream()
                        .filter(appointment -> "Completed".equalsIgnoreCase(appointment.getStatus()))
                        .collect(Collectors.toList());
                break;
                
            case TAB_CANCELLED:
                filteredList = allAppointments.stream()
                        .filter(appointment -> "Cancelled".equalsIgnoreCase(appointment.getStatus()))
                        .collect(Collectors.toList());
                break;
                
            default:
                filteredList = allAppointments;
                break;
        }
        
        updateUI(filteredList);
    }
    
    private void updateUI(List<Reservation> appointments) {
        if (appointments.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvAppointments.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvAppointments.setVisibility(View.VISIBLE);
            appointmentAdapter.setAppointmentList(appointments);
        }
    }
    
    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        if (isLoading) {
            rvAppointments.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.GONE);
        }
    }
    
    private void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        tvEmpty.setText(R.string.error);
        tvEmpty.setVisibility(View.VISIBLE);
        rvAppointments.setVisibility(View.GONE);
    }
    
    @Override
    public void onCancelAppointment(Reservation reservation) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận hủy lịch")
                .setMessage("Bạn có chắc chắn muốn hủy lịch khám này?")
                .setPositiveButton("Hủy lịch", (dialog, which) -> {
                    cancelAppointment(reservation);
                })
                .setNegativeButton("Không", null)
                .show();
    }
    
    private void cancelAppointment(Reservation reservation) {
        showLoading(true);
        
        reservationViewModel.updateReservationStatus(reservation.getReservationId(), "Cancelled")
                .observe(getViewLifecycleOwner(), result -> {
                    showLoading(false);
                    
                    if (result.status == Resource.Status.SUCCESS) {
                        Toast.makeText(requireContext(), "Đã hủy lịch hẹn thành công", Toast.LENGTH_SHORT).show();
                        loadAppointments(); // Tải lại danh sách
                    } else if (result.status == Resource.Status.ERROR) {
                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
    @Override
    public void onRescheduleAppointment(Reservation reservation) {
        Intent intent = new Intent(requireContext(), RescheduleAppointmentActivity.class);
        intent.putExtra(Constants.EXTRA_RESERVATION_ID, reservation.getReservationId());
        startActivity(intent);
    }
    
    @Override
    public void onAppointmentClick(Reservation reservation) {
        Intent intent = new Intent(requireContext(), AppointmentDetailActivity.class);
        intent.putExtra(Constants.EXTRA_RESERVATION_ID, reservation.getReservationId());
        startActivity(intent);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Tải lại dữ liệu khi quay lại fragment (sau khi đã hủy hoặc đổi lịch)
        loadAppointments();
    }
} 