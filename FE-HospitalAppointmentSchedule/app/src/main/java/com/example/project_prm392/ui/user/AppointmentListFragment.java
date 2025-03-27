package com.example.project_prm392.ui.user;

import android.app.AlertDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.project_prm392.R;
import com.example.project_prm392.adapters.AppointmentAdapter;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.ReservationResponse;
import com.example.project_prm392.repository.ReservationRepository;
import com.example.project_prm392.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class AppointmentListFragment extends Fragment implements AppointmentAdapter.OnAppointmentActionListener {

    private static final String ARG_STATUS_TYPE = "status_type";
    
    public static final String TYPE_UPCOMING = "upcoming";
    public static final String TYPE_PAST = "past";
    public static final String TYPE_CANCELLED = "cancelled";
    
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewAppointments;
    private TextView tvNoAppointments;
    private ProgressBar progressBar;
    
    private AppointmentAdapter appointmentAdapter;
    private List<ReservationResponse> appointmentsList = new ArrayList<>();
    private String statusType;
    
    @Inject
    ReservationRepository reservationRepository;
    
    @Inject
    SessionManager sessionManager;
    
    public static AppointmentListFragment newInstance(String statusType) {
        AppointmentListFragment fragment = new AppointmentListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STATUS_TYPE, statusType);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            statusType = getArguments().getString(ARG_STATUS_TYPE);
        }
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment_list, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerViewAppointments = view.findViewById(R.id.recyclerViewAppointments);
        tvNoAppointments = view.findViewById(R.id.tvNoAppointments);
        progressBar = view.findViewById(R.id.progressBar);
        
        recyclerViewAppointments.setLayoutManager(new LinearLayoutManager(requireContext()));
        appointmentAdapter = new AppointmentAdapter(appointmentsList);
        appointmentAdapter.setOnAppointmentActionListener(this);
        recyclerViewAppointments.setAdapter(appointmentAdapter);
        
        swipeRefreshLayout.setOnRefreshListener(this::loadAppointments);
        
        loadAppointments();
    }
    
    private void loadAppointments() {
        progressBar.setVisibility(View.VISIBLE);
        tvNoAppointments.setVisibility(View.GONE);
        
        int patientId = sessionManager.getUserId();
        
        reservationRepository.getReservationsByPatient(patientId).enqueue(new Callback<BaseResponse<List<ReservationResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ReservationResponse>>> call, Response<BaseResponse<List<ReservationResponse>>> response) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<ReservationResponse> allAppointments = response.body().getData();
                    
                    // Filter appointments based on status type
                    List<ReservationResponse> filteredAppointments = new ArrayList<>();
                    
                    switch (statusType) {
                        case TYPE_UPCOMING:
                            filteredAppointments = allAppointments.stream()
                                .filter(appointment -> 
                                    appointment.getStatus().equalsIgnoreCase("confirmed") || 
                                    appointment.getStatus().equalsIgnoreCase("approved") || 
                                    appointment.getStatus().equalsIgnoreCase("pending"))
                                .collect(Collectors.toList());
                            break;
                        case TYPE_PAST:
                            filteredAppointments = allAppointments.stream()
                                .filter(appointment -> appointment.getStatus().equalsIgnoreCase("completed"))
                                .collect(Collectors.toList());
                            break;
                        case TYPE_CANCELLED:
                            filteredAppointments = allAppointments.stream()
                                .filter(appointment -> appointment.getStatus().equalsIgnoreCase("cancelled"))
                                .collect(Collectors.toList());
                            break;
                    }
                    
                    appointmentsList.clear();
                    appointmentsList.addAll(filteredAppointments);
                    appointmentAdapter.notifyDataSetChanged();
                    
                    if (appointmentsList.isEmpty()) {
                        tvNoAppointments.setVisibility(View.VISIBLE);
                        recyclerViewAppointments.setVisibility(View.GONE);
                    } else {
                        tvNoAppointments.setVisibility(View.GONE);
                        recyclerViewAppointments.setVisibility(View.VISIBLE);
                    }
                } else {
                    tvNoAppointments.setVisibility(View.VISIBLE);
                    recyclerViewAppointments.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), "Failed to load appointments", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<BaseResponse<List<ReservationResponse>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                tvNoAppointments.setVisibility(View.VISIBLE);
                recyclerViewAppointments.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    public void onRescheduleClick(int position) {
        // Navigate to reschedule screen
        ReservationResponse appointment = appointmentsList.get(position);
        // Will be implemented in the corresponding activity
    }
    
    @Override
    public void onCancelClick(int position) {
        ReservationResponse appointment = appointmentsList.get(position);
        
        // Show dialog to confirm cancellation
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_cancel_appointment, null);
        TextInputEditText etCancelReason = dialogView.findViewById(R.id.etCancelReason);
        
        new AlertDialog.Builder(requireContext())
            .setTitle("Cancel Appointment")
            .setMessage("Are you sure you want to cancel this appointment?")
            .setView(dialogView)
            .setPositiveButton("Confirm", (dialog, which) -> {
                String reason = etCancelReason.getText().toString().trim();
                if (reason.isEmpty()) {
                    reason = "Cancelled by patient";
                }
                
                cancelAppointment(appointment.getReservationId(), reason);
            })
            .setNegativeButton("No", null)
            .show();
    }
    
    private void cancelAppointment(int reservationId, String reason) {
        progressBar.setVisibility(View.VISIBLE);
        
        reservationRepository.cancelReservation(reservationId, reason).enqueue(new Callback<BaseResponse<ReservationResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<ReservationResponse>> call, Response<BaseResponse<ReservationResponse>> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(requireContext(), "Appointment cancelled successfully", Toast.LENGTH_SHORT).show();
                    loadAppointments();
                } else {
                    Toast.makeText(requireContext(), "Failed to cancel appointment", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<BaseResponse<ReservationResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    public void onItemClick(int position) {
        ReservationResponse appointment = appointmentsList.get(position);
        // Navigate to appointment details
        // Will be implemented in the corresponding activity
    }
} 