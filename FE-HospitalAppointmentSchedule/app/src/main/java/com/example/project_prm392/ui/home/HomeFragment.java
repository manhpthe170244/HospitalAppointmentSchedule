package com.example.project_prm392.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.adapter.DoctorAdapter;
import com.example.project_prm392.adapter.ServiceAdapter;
import com.example.project_prm392.model.Doctor;
import com.example.project_prm392.model.Reservation;
import com.example.project_prm392.model.Service;
import com.example.project_prm392.ui.doctor.DoctorDetailActivity;
import com.example.project_prm392.ui.doctor.DoctorListFragment;
import com.example.project_prm392.ui.reservation.AppointmentDetailActivity;
import com.example.project_prm392.ui.service.ServiceDetailActivity;
import com.example.project_prm392.ui.service.ServiceListFragment;
import com.example.project_prm392.utils.Constants;
import com.example.project_prm392.utils.Resource;
import com.example.project_prm392.viewmodel.DoctorViewModel;
import com.example.project_prm392.viewmodel.ReservationViewModel;
import com.example.project_prm392.viewmodel.ServiceViewModel;
import com.example.project_prm392.viewmodel.ViewModelFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment implements DoctorAdapter.OnDoctorClickListener, ServiceAdapter.OnServiceClickListener {
    private SearchView searchView;
    private RecyclerView rvFeaturedDoctors, rvPopularServices;
    private TextView tvViewAllDoctors, tvViewAllServices;
    private CardView cardNextAppointment;
    private TextView tvAppointmentDate, tvAppointmentTime, tvAppointmentDoctor, tvAppointmentService;
    private Button btnViewAppointment;
    
    private DoctorViewModel doctorViewModel;
    private ServiceViewModel serviceViewModel;
    private ReservationViewModel reservationViewModel;
    
    private DoctorAdapter doctorAdapter;
    private ServiceAdapter serviceAdapter;
    
    private SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private SimpleDateFormat outputTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    
    private Reservation nextAppointment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        // Khởi tạo ViewModels
        ViewModelFactory factory = ViewModelFactory.getInstance();
        doctorViewModel = new ViewModelProvider(requireActivity(), factory).get(DoctorViewModel.class);
        serviceViewModel = new ViewModelProvider(requireActivity(), factory).get(ServiceViewModel.class);
        reservationViewModel = new ViewModelProvider(requireActivity(), factory).get(ReservationViewModel.class);
        
        // Khởi tạo Views
        initViews(view);
        
        // Thiết lập listeners
        setupListeners();
        
        // Thiết lập RecyclerViews
        setupRecyclerViews();
        
        // Load dữ liệu
        loadData();
        
        return view;
    }
    
    private void initViews(View view) {
        searchView = view.findViewById(R.id.searchView);
        rvFeaturedDoctors = view.findViewById(R.id.rvFeaturedDoctors);
        rvPopularServices = view.findViewById(R.id.rvPopularServices);
        tvViewAllDoctors = view.findViewById(R.id.tvViewAllDoctors);
        tvViewAllServices = view.findViewById(R.id.tvViewAllServices);
        cardNextAppointment = view.findViewById(R.id.cardNextAppointment);
        tvAppointmentDate = view.findViewById(R.id.tvAppointmentDate);
        tvAppointmentTime = view.findViewById(R.id.tvAppointmentTime);
        tvAppointmentDoctor = view.findViewById(R.id.tvAppointmentDoctor);
        tvAppointmentService = view.findViewById(R.id.tvAppointmentService);
        btnViewAppointment = view.findViewById(R.id.btnViewAppointment);
    }
    
    private void setupRecyclerViews() {
        // RecyclerView bác sĩ
        doctorAdapter = new DoctorAdapter(requireContext(), this);
        rvFeaturedDoctors.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        rvFeaturedDoctors.setAdapter(doctorAdapter);
        
        // RecyclerView dịch vụ
        serviceAdapter = new ServiceAdapter(requireContext(), this);
        rvPopularServices.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        rvPopularServices.setAdapter(serviceAdapter);
    }
    
    private void setupListeners() {
        // SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }
            
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        
        // Xem tất cả bác sĩ
        tvViewAllDoctors.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new DoctorListFragment())
                    .addToBackStack(null)
                    .commit();
        });
        
        // Xem tất cả dịch vụ
        tvViewAllServices.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ServiceListFragment())
                    .addToBackStack(null)
                    .commit();
        });
        
        // Xem chi tiết lịch hẹn
        btnViewAppointment.setOnClickListener(v -> {
            if (nextAppointment != null) {
                Intent intent = new Intent(requireContext(), AppointmentDetailActivity.class);
                intent.putExtra(Constants.EXTRA_RESERVATION_ID, nextAppointment.getReservationId());
                startActivity(intent);
            }
        });
    }
    
    private void loadData() {
        // Load danh sách bác sĩ nổi bật
        doctorViewModel.getAllDoctors().observe(getViewLifecycleOwner(), result -> {
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                List<Doctor> topDoctors = result.data.stream()
                        .limit(5) // Lấy 5 bác sĩ đầu tiên
                        .collect(Collectors.toList());
                doctorAdapter.setDoctorList(topDoctors);
            } else if (result.status == Resource.Status.ERROR) {
                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show();
            }
        });
        
        // Load danh sách dịch vụ phổ biến
        serviceViewModel.getAllServices().observe(getViewLifecycleOwner(), result -> {
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                List<Service> topServices = result.data.stream()
                        .limit(5) // Lấy 5 dịch vụ đầu tiên
                        .collect(Collectors.toList());
                serviceAdapter.setServiceList(topServices);
            } else if (result.status == Resource.Status.ERROR) {
                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show();
            }
        });
        
        // Load lịch hẹn sắp tới
        reservationViewModel.getReservationsByPatient().observe(getViewLifecycleOwner(), result -> {
            if (result.status == Resource.Status.SUCCESS && result.data != null && !result.data.isEmpty()) {
                // Lọc lịch hẹn sắp tới (Pending hoặc Confirmed)
                List<Reservation> upcomingAppointments = result.data.stream()
                        .filter(appointment -> "Pending".equalsIgnoreCase(appointment.getStatus()) || 
                                               "Confirmed".equalsIgnoreCase(appointment.getStatus()))
                        .collect(Collectors.toList());
                
                if (!upcomingAppointments.isEmpty()) {
                    // Lấy lịch hẹn gần nhất
                    nextAppointment = upcomingAppointments.get(0);
                    updateNextAppointmentUI(nextAppointment);
                    cardNextAppointment.setVisibility(View.VISIBLE);
                } else {
                    cardNextAppointment.setVisibility(View.GONE);
                }
            } else {
                cardNextAppointment.setVisibility(View.GONE);
            }
        });
    }
    
    private void updateNextAppointmentUI(Reservation appointment) {
        try {
            Date appointmentDate = inputDateFormat.parse(appointment.getAppointmentDate());
            if (appointmentDate != null) {
                tvAppointmentDate.setText("Ngày " + outputDateFormat.format(appointmentDate));
                tvAppointmentTime.setText(outputTimeFormat.format(appointmentDate) + " - " + 
                        outputTimeFormat.format(new Date(appointmentDate.getTime() + 30 * 60 * 1000))); // Giả sử thời gian khám là 30 phút
            }
        } catch (ParseException e) {
            tvAppointmentDate.setText("Ngày không xác định");
            tvAppointmentTime.setText("Giờ không xác định");
        }
        
        // Thông tin bác sĩ
        if (appointment.getDoctor() != null && appointment.getDoctor().getDoctorNavigation() != null) {
            tvAppointmentDoctor.setText("Bác sĩ: " + appointment.getDoctor().getDoctorNavigation().getFullName());
        } else {
            tvAppointmentDoctor.setText("Bác sĩ: Không xác định");
        }
        
        // Thông tin dịch vụ
        if (appointment.getService() != null) {
            tvAppointmentService.setText("Dịch vụ: " + appointment.getService().getServiceName());
        } else {
            tvAppointmentService.setText("Dịch vụ: Không xác định");
        }
    }
    
    private void performSearch(String query) {
        // Chuyển đến màn hình tìm kiếm với query
        Toast.makeText(requireContext(), "Tìm kiếm: " + query, Toast.LENGTH_SHORT).show();
        // TODO: Điều hướng đến màn hình tìm kiếm
    }
    
    @Override
    public void onDoctorClick(Doctor doctor) {
        // Chuyển sang màn hình chi tiết bác sĩ
        Intent intent = new Intent(requireContext(), DoctorDetailActivity.class);
        intent.putExtra(Constants.EXTRA_DOCTOR_ID, doctor.getDoctorId());
        startActivity(intent);
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
    
    @Override
    public void onResume() {
        super.onResume();
        // Tải lại dữ liệu khi quay lại fragment
        loadData();
    }
} 