package com.example.project_prm392.ui.user;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
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
import com.example.project_prm392.network.ApiService;
import com.example.project_prm392.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class BookAppointmentActivity extends AppCompatActivity implements TimeSlotAdapter.OnTimeSlotSelectedListener {

    private Toolbar toolbar;
    private TextView tvDoctorName, tvSpecialty;
    private CalendarView calendarView;
    private RecyclerView recyclerViewTimeSlots;
    private EditText etReason;
    private Button btnConfirmAppointment;
    private ProgressBar progressBar;
    
    private TimeSlotAdapter timeSlotAdapter;
    private List<SlotResponse> timeSlots = new ArrayList<>();
    
    private int doctorId;
    private int serviceId = 1; // Default service ID, would be replaced with actual service selection
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
    
    @Inject
    ApiService apiService;
    

    private EditText etDate;

    private Spinner spinnerService;
    private Spinner spinnerSchedule;
    private Button btnBook;
    

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault());
    private final SimpleDateFormat displayDateFormat = new SimpleDateFormat(Constants.DISPLAY_DATE_FORMAT, Locale.getDefault());
    
    private List<ServiceResponse> services = new ArrayList<>();
    private List<DoctorScheduleResponse> schedules = new ArrayList<>();
    private int selectedServiceId = -1;
    private int selectedScheduleId = -1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        
        // Get doctor ID from intent if available
        doctorId = getIntent().getIntExtra("doctorId", -1);
        
        initViews();
        setupToolbar();
        setupCalendar();
        setupRecyclerView();
        
        if (doctorId != -1) {
            loadDoctorDetails();
        }
        
        btnConfirmAppointment.setOnClickListener(v -> bookAppointment());
        
        // Get data from intent
        String doctorName = getIntent().getStringExtra("doctorName");
        String specialty = getIntent().getStringExtra("specialty");
        
        if (doctorName != null) {
            tvDoctorName.setText(doctorName);
            tvSpecialty.setText(specialty);
        }
        
        // Set up spinners
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
        
        // Set up book button
        btnBook.setOnClickListener(v -> bookAppointment());
        
        // Load services and schedules
        loadServices();
        loadSchedules();
    }
    
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvSpecialty = findViewById(R.id.tvSpecialty);
        calendarView = findViewById(R.id.calendarView);
        recyclerViewTimeSlots = findViewById(R.id.recyclerViewTimeSlots);
        etReason = findViewById(R.id.etReason);
        btnConfirmAppointment = findViewById(R.id.btnConfirmAppointment);
        progressBar = findViewById(R.id.progressBar);
        spinnerService = findViewById(R.id.spinnerService);
        spinnerSchedule = findViewById(R.id.spinnerSchedule);
        btnBook = findViewById(R.id.btnBook);
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void setupCalendar() {
        // Set minimum date to today
        Calendar calendar = Calendar.getInstance();
        long today = calendar.getTimeInMillis();
        calendarView.setMinDate(today);
        
        // Set maximum date to 3 months from now
        calendar.add(Calendar.MONTH, 3);
        long maxDate = calendar.getTimeInMillis();
        calendarView.setMaxDate(maxDate);
        
        // Set default date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = sdf.format(new Date(today));
        
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar selected = Calendar.getInstance();
            selected.set(year, month, dayOfMonth);
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            selectedDate = dateFormat.format(selected.getTime());
            
            // Load available time slots for the selected date
            if (doctorId != -1) {
                loadTimeSlots();
            }
        });
    }
    
    private void setupRecyclerView() {
        recyclerViewTimeSlots.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        timeSlotAdapter = new TimeSlotAdapter(timeSlots);
        timeSlotAdapter.setOnTimeSlotSelectedListener(this);
        recyclerViewTimeSlots.setAdapter(timeSlotAdapter);
    }
    
    private void loadDoctorDetails() {
        progressBar.setVisibility(View.VISIBLE);
        
        doctorRepository.getDoctorById(doctorId).enqueue(new Callback<BaseResponse<DoctorDetailsResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<DoctorDetailsResponse>> call, Response<BaseResponse<DoctorDetailsResponse>> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    DoctorDetailsResponse doctor = response.body().getData();
                    
                    tvDoctorName.setText(doctor.getUserName());
                    
                    if (doctor.getSpecialties() != null && !doctor.getSpecialties().isEmpty()) {
                        tvSpecialty.setText(doctor.getSpecialties().get(0).getSpecialtyName());
                        
                        // Assuming the first service for the first specialty
                        if (doctor.getServices() != null && !doctor.getServices().isEmpty()) {
                            serviceId = doctor.getServices().get(0).getServiceId();
                        }
                    }
                    
                    // Load available time slots
                    loadTimeSlots();
                } else {
                    Toast.makeText(BookAppointmentActivity.this, "Failed to load doctor details", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<BaseResponse<DoctorDetailsResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(BookAppointmentActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void loadTimeSlots() {
        progressBar.setVisibility(View.VISIBLE);
        
        // In a real app, you would fetch available schedules and slots from the API
        // For this example, we'll create some dummy time slots
        timeSlots.clear();
        
        // Create dummy time slots
        for (int i = 9; i < 17; i++) {
            SlotResponse slot = new SlotResponse();
            slot.setSlotId(i - 8);
            slot.setStartTime(String.format(Locale.getDefault(), "%02d:00", i));
            slot.setEndTime(String.format(Locale.getDefault(), "%02d:30", i));
            slot.setAvailable(true);
            timeSlots.add(slot);
            
            SlotResponse slot2 = new SlotResponse();
            slot2.setSlotId(i - 8 + 10);
            slot2.setStartTime(String.format(Locale.getDefault(), "%02d:30", i));
            slot2.setEndTime(String.format(Locale.getDefault(), "%02d:00", i + 1));
            slot2.setAvailable(i != 12); // Make 12:30-13:00 unavailable as an example
            timeSlots.add(slot2);
        }
        
        // In a real app, you would set a default schedule ID based on the doctor's schedules
        selectedDoctorScheduleId = 1;
        
        timeSlotAdapter.setTimeSlots(timeSlots);
        progressBar.setVisibility(View.GONE);
    }
    
    private void loadServices() {
        progressBar.setVisibility(View.VISIBLE);
        
        apiService.getServicesBySpecialty(1).enqueue(new Callback<BaseResponse<List<ServiceResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<ServiceResponse>>> call, Response<BaseResponse<List<ServiceResponse>>> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    services = response.body().getData();
                    
                    // Create spinner options
                    List<String> serviceNames = new ArrayList<>();
                    serviceNames.add("Select Service");
                    for (ServiceResponse service : services) {
                        serviceNames.add(service.getServiceName() + " - $" + service.getPrice());
                    }
                    
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            BookAppointmentActivity.this,
                            android.R.layout.simple_spinner_item,
                            serviceNames
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerService.setAdapter(adapter);
                } else {
                    Toast.makeText(BookAppointmentActivity.this, "Error loading services", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<BaseResponse<List<ServiceResponse>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(BookAppointmentActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void loadSchedules() {
        progressBar.setVisibility(View.VISIBLE);
        
        apiService.getDoctorSchedules(doctorId, dateFormat.format(selectedDate.getTime())).enqueue(new Callback<BaseResponse<List<DoctorScheduleResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<DoctorScheduleResponse>>> call, Response<BaseResponse<List<DoctorScheduleResponse>>> response) {
                progressBar.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    schedules = response.body().getData();
                    
                    // Create spinner options
                    List<String> scheduleOptions = new ArrayList<>();
                    scheduleOptions.add("Select Time Slot");
                    for (DoctorScheduleResponse schedule : schedules) {
                        scheduleOptions.add(schedule.getDayOfWeek() + " - " + schedule.getSlotTime());
                    }
                    
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            BookAppointmentActivity.this,
                            android.R.layout.simple_spinner_item,
                            scheduleOptions
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerSchedule.setAdapter(adapter);
                } else {
                    Toast.makeText(BookAppointmentActivity.this, "Error loading schedules", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<BaseResponse<List<DoctorScheduleResponse>>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(BookAppointmentActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void bookAppointment() {
        // Validate inputs
        if (selectedServiceId == -1) {
            Toast.makeText(this, "Please select a service", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (selectedScheduleId == -1) {
            Toast.makeText(this, "Please select a time slot", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (etDate.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String reason = etReason.getText().toString().trim();
        if (reason.isEmpty()) {
            Toast.makeText(this, "Please enter a reason for visit", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Create reservation request
        ReservationCreateRequest request = new ReservationCreateRequest();
        request.setPatientId(sessionManager.getUserId());
        request.setReason(reason);
        request.setAppointmentDate(dateFormat.format(selectedDate.getTime()));
        request.setDoctorScheduleId(selectedScheduleId);
        
        // Show progress
        progressBar.setVisibility(View.VISIBLE);
        btnBook.setEnabled(false);
        
        // Make API call
        apiService.createReservation(request).enqueue(new Callback<BaseResponse<ReservationResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<ReservationResponse>> call, Response<BaseResponse<ReservationResponse>> response) {
                progressBar.setVisibility(View.GONE);
                btnBook.setEnabled(true);
                
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(BookAppointmentActivity.this, "Appointment booked successfully", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    String errorMessage = "Failed to book appointment";
                    if (response.body() != null) {
                        errorMessage = response.body().getMessage();
                    }
                    Toast.makeText(BookAppointmentActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
            
            @Override
            public void onFailure(Call<BaseResponse<ReservationResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnBook.setEnabled(true);
                Toast.makeText(BookAppointmentActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    
    @Override
    public void onTimeSlotSelected(int position) {
        // This is called when a time slot is selected in the adapter
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
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