package com.example.project_prm392.ui.user;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.adapters.TimeSlotAdapter;
import com.example.project_prm392.models.requests.ReservationCreateRequest;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.DoctorDetailsResponse;
import com.example.project_prm392.models.responses.DoctorResponse;
import com.example.project_prm392.models.responses.DoctorScheduleResponse;
import com.example.project_prm392.models.responses.ReservationResponse;
import com.example.project_prm392.models.responses.ServiceResponse;
import com.example.project_prm392.models.responses.SlotResponse;
import com.example.project_prm392.repository.DoctorRepository;
import com.example.project_prm392.repository.ReservationRepository;
import com.example.project_prm392.repository.ServiceRepository;
import com.example.project_prm392.utils.SessionManager;
import com.example.project_prm392.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BookAppointmentActivity extends AppCompatActivity implements TimeSlotAdapter.OnTimeSlotSelectedListener {

    private Toolbar toolbar;
    private TextView tvDoctorName, tvSpecialty;
    private RecyclerView recyclerViewTimeSlots;
    private EditText etReason;
    private Button btnConfirmAppointment;
    private ProgressBar progressBar;
    
    private TimeSlotAdapter timeSlotAdapter;
    private final List<SlotResponse> timeSlots = new ArrayList<>();
    
    private int doctorId;
    private int serviceId = 1; // Default service ID
    private String selectedDate;
    private int selectedDoctorScheduleId;
    
    @Inject
    DoctorRepository doctorRepository;
    
    @Inject
    ServiceRepository serviceRepository;
    
    @Inject
    ReservationRepository reservationRepository;
    
    @Inject
    SessionManager sessionManager;

    private EditText etDate;
    private Spinner spinnerService;
    private Spinner spinnerSchedule;
    private Button btnBook;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());
    private final SimpleDateFormat displayDateFormat = new SimpleDateFormat(Constants.DISPLAY_DATE_FORMAT, Locale.getDefault());
    
    private final List<ServiceResponse> services = new ArrayList<>();
    private final List<DoctorScheduleResponse> schedules = new ArrayList<>();
    private int selectedServiceId = -1;
    private int selectedScheduleId = -1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        
        doctorId = getIntent().getIntExtra("doctorId", -1);
        
        initViews();
        setupToolbar();
        setupDatePicker();
        setupRecyclerView();
        
        if (doctorId != -1) {
            loadDoctorDetails();
        }
        
        String doctorName = getIntent().getStringExtra("doctorName");
        String specialty = getIntent().getStringExtra("specialty");
        
        if (doctorName != null) {
            tvDoctorName.setText(doctorName);
            tvSpecialty.setText(specialty);
        }
        
        setupSpinners();
        setupButtons();
        
        loadServices();
        loadSchedules();
    }
    
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvSpecialty = findViewById(R.id.tvSpecialty);
        recyclerViewTimeSlots = findViewById(R.id.recyclerViewTimeSlots);
        etReason = findViewById(R.id.etReason);
        btnConfirmAppointment = findViewById(R.id.btnConfirmAppointment);
        progressBar = findViewById(R.id.progressBar);
        
        spinnerService = findViewById(R.id.spinnerService);
        spinnerSchedule = findViewById(R.id.spinnerSchedule);
        btnBook = findViewById(R.id.btnBook);
        etDate = findViewById(R.id.etDate);
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Book Appointment");
        }
    }
    
    private void setupDatePicker() {
        Calendar calendar = Calendar.getInstance();
        etDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    
                    selectedDate = dateFormat.format(calendar.getTime());
                    etDate.setText(displayDateFormat.format(calendar.getTime()));
                    
                    if (doctorId != -1) {
                        loadTimeSlots();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            );
            
            // Set min date to today
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            
            // Set max date to 3 months from now
            Calendar maxDate = Calendar.getInstance();
            maxDate.add(Calendar.MONTH, 3);
            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
            
            datePickerDialog.show();
        });
    }
    
    private void setupRecyclerView() {
        recyclerViewTimeSlots.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        timeSlotAdapter = new TimeSlotAdapter(timeSlots);
        timeSlotAdapter.setOnTimeSlotSelectedListener(this);
        recyclerViewTimeSlots.setAdapter(timeSlotAdapter);
    }
    
    private void setupSpinners() {
        // Service spinner
        spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedServiceId = services.get(position - 1).getServiceId();
                } else {
                    selectedServiceId = -1;
                }
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedServiceId = -1;
            }
        });
        
        // Schedule spinner
        spinnerSchedule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    selectedScheduleId = schedules.get(position - 1).getDoctorScheduleId();
                } else {
                    selectedScheduleId = -1;
                }
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedScheduleId = -1;
            }
        });
    }
    
    private void setupButtons() {
        btnBook.setOnClickListener(v -> bookAppointment());
        btnConfirmAppointment.setOnClickListener(v -> bookAppointment());
    }
    
    private void loadDoctorDetails() {
        progressBar.setVisibility(View.VISIBLE);
        
        doctorRepository.getDoctorById(doctorId).observe(this, response -> {
            progressBar.setVisibility(View.GONE);
            
            if (response != null && response.isSuccess()) {
                DoctorDetailsResponse doctor = response.getData();
                
                tvDoctorName.setText(doctor.getUserName());
                
                if (doctor.getSpecialties() != null && !doctor.getSpecialties().isEmpty()) {
                    tvSpecialty.setText(doctor.getSpecialties().get(0).getSpecialtyName());
                }
            } else {
                String message = response != null ? response.getMessage() : "Failed to load doctor details";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void loadTimeSlots() {
        if (selectedDate == null || selectedScheduleId == -1) {
            return;
        }
        
        progressBar.setVisibility(View.VISIBLE);
        timeSlots.clear();
        timeSlotAdapter.notifyDataSetChanged();
        
        doctorRepository.getDoctorTimeSlots(doctorId, selectedDate).observe(this, response -> {
            progressBar.setVisibility(View.GONE);
            
            if (response != null && response.isSuccess()) {
                List<SlotResponse> slots = response.getData();
                if (slots != null) {
                    timeSlots.addAll(slots);
                    timeSlotAdapter.notifyDataSetChanged();
                }
            } else {
                String message = response != null ? response.getMessage() : "Failed to load time slots";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void loadServices() {
        progressBar.setVisibility(View.VISIBLE);
        
        serviceRepository.getAllServices().observe(this, response -> {
            progressBar.setVisibility(View.GONE);
            
            if (response != null && response.isSuccess()) {
                List<ServiceResponse> serviceList = response.getData();
                if (serviceList != null) {
                    services.clear();
                    services.addAll(serviceList);
                    
                    List<String> serviceNames = new ArrayList<>();
                    serviceNames.add("Select a service");
                    for (ServiceResponse service : services) {
                        serviceNames.add(service.getServiceName());
                    }
                    
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        serviceNames
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerService.setAdapter(adapter);
                }
            } else {
                String message = response != null ? response.getMessage() : "Failed to load services";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void loadSchedules() {
        if (doctorId == -1) {
            return;
        }
        
        progressBar.setVisibility(View.VISIBLE);
        
        doctorRepository.getDoctorSchedules(doctorId).observe(this, response -> {
            progressBar.setVisibility(View.GONE);
            
            if (response != null && response.isSuccess()) {
                List<DoctorScheduleResponse> scheduleList = response.getData();
                if (scheduleList != null) {
                    schedules.clear();
                    schedules.addAll(scheduleList);
                    
                    List<String> scheduleNames = new ArrayList<>();
                    scheduleNames.add("Select a schedule");
                    for (DoctorScheduleResponse schedule : schedules) {
                        scheduleNames.add(schedule.getDay() + " " + schedule.getStartTime() + " - " + schedule.getEndTime());
                    }
                    
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        scheduleNames
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSchedule.setAdapter(adapter);
                }
            } else {
                String message = response != null ? response.getMessage() : "Failed to load schedules";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void bookAppointment() {
        if (selectedServiceId == -1) {
            Toast.makeText(this, "Please select a service", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (selectedDate == null) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (selectedScheduleId == -1) {
            Toast.makeText(this, "Please select a schedule", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String reason = etReason.getText().toString().trim();
        if (reason.isEmpty()) {
            etReason.setError("Please enter a reason");
            etReason.requestFocus();
            return;
        }
        
        progressBar.setVisibility(View.VISIBLE);
        btnBook.setEnabled(false);
        btnConfirmAppointment.setEnabled(false);
        
        ReservationCreateRequest request = new ReservationCreateRequest(
            sessionManager.getUserId(),
            doctorId,
            selectedServiceId,
            selectedScheduleId,
            selectedDate,
            reason
        );
        
        reservationRepository.createReservation(request).observe(this, response -> {
            progressBar.setVisibility(View.GONE);
            btnBook.setEnabled(true);
            btnConfirmAppointment.setEnabled(true);
            
            if (response != null && response.isSuccess()) {
                Toast.makeText(this, "Appointment booked successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                String message = response != null ? response.getMessage() : "Failed to book appointment";
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    public void onTimeSlotSelected(SlotResponse slot) {
        selectedScheduleId = slot.getDoctorScheduleId();
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 