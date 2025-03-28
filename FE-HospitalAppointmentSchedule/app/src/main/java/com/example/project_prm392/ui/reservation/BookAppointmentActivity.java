package com.example.project_prm392.ui.reservation;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_prm392.MainActivity;
import com.example.project_prm392.R;
import com.example.project_prm392.adapter.TimeSlotAdapter;
import com.example.project_prm392.model.Doctor;
import com.example.project_prm392.model.Reservation;
import com.example.project_prm392.model.Service;
import com.example.project_prm392.model.TimeSlot;
import com.example.project_prm392.utils.Constants;
import com.example.project_prm392.utils.Resource;
import com.example.project_prm392.utils.SessionManager;
import com.example.project_prm392.viewmodel.DoctorViewModel;
import com.example.project_prm392.viewmodel.ReservationViewModel;
import com.example.project_prm392.viewmodel.ServiceViewModel;
import com.example.project_prm392.viewmodel.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookAppointmentActivity extends AppCompatActivity implements TimeSlotAdapter.OnTimeSlotClickListener {
    private ImageView ivDoctorImage;
    private TextView tvDoctorName, tvDoctorSpecialty;
    private TextView tvServiceName, tvServicePrice, tvServiceDuration;
    private CalendarView calendarView;
    private RecyclerView rvTimeSlots;
    private TextInputEditText etNote;
    private Button btnBookAppointment;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    
    private DoctorViewModel doctorViewModel;
    private ServiceViewModel serviceViewModel;
    private ReservationViewModel reservationViewModel;
    private TimeSlotAdapter timeSlotAdapter;
    private SessionManager sessionManager;
    
    private int doctorId;
    private int serviceId;
    private Doctor doctor;
    private Service service;
    private Date selectedDate;
    private String selectedTime;
    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        
        // Lấy doctorId và serviceId từ intent
        doctorId = getIntent().getIntExtra(Constants.EXTRA_DOCTOR_ID, -1);
        serviceId = getIntent().getIntExtra(Constants.EXTRA_SERVICE_ID, -1);
        
        if (doctorId == -1 || serviceId == -1) {
            Toast.makeText(this, "Không đủ thông tin để đặt lịch", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Khởi tạo ViewModel
        ViewModelFactory factory = ViewModelFactory.getInstance();
        doctorViewModel = new ViewModelProvider(this, factory).get(DoctorViewModel.class);
        serviceViewModel = new ViewModelProvider(this, factory).get(ServiceViewModel.class);
        reservationViewModel = new ViewModelProvider(this, factory).get(ReservationViewModel.class);
        
        // Khởi tạo SessionManager
        sessionManager = SessionManager.getInstance();
        
        // Khởi tạo Views
        initViews();
        
        // Thiết lập Toolbar
        setupToolbar();
        
        // Thiết lập RecyclerView
        setupRecyclerView();
        
        // Thiết lập Calendar
        setupCalendarView();
        
        // Thiết lập listeners
        setupListeners();
        
        // Load dữ liệu
        loadData();
    }
    
    private void initViews() {
        ivDoctorImage = findViewById(R.id.ivDoctorImage);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvDoctorSpecialty = findViewById(R.id.tvDoctorSpecialty);
        tvServiceName = findViewById(R.id.tvServiceName);
        tvServicePrice = findViewById(R.id.tvServicePrice);
        tvServiceDuration = findViewById(R.id.tvServiceDuration);
        calendarView = findViewById(R.id.calendarView);
        rvTimeSlots = findViewById(R.id.rvTimeSlots);
        etNote = findViewById(R.id.etNote);
        btnBookAppointment = findViewById(R.id.btnBookAppointment);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void setupRecyclerView() {
        timeSlotAdapter = new TimeSlotAdapter(this, this);
        rvTimeSlots.setLayoutManager(new GridLayoutManager(this, 4)); // 4 cột
        rvTimeSlots.setAdapter(timeSlotAdapter);
    }
    
    private void setupCalendarView() {
        // Thiết lập ngày tối thiểu (ngày hiện tại)
        Calendar calendar = Calendar.getInstance();
        long today = calendar.getTimeInMillis();
        calendarView.setMinDate(today);
        
        // Thiết lập ngày tối đa (30 ngày sau)
        calendar.add(Calendar.DAY_OF_MONTH, 30);
        calendarView.setMaxDate(calendar.getTimeInMillis());
        
        // Lấy ngày hiện tại
        selectedDate = new Date(today);
        
        // Thiết lập listener
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            selectedDate = calendar.getTime();
            
            // Tải danh sách slot khả dụng cho ngày đã chọn
            loadTimeSlots();
        });
    }
    
    private void setupListeners() {
        btnBookAppointment.setOnClickListener(v -> createReservation());
    }
    
    private void loadData() {
        showLoading(true);
        
        // Load thông tin bác sĩ
        doctorViewModel.getDoctorById(doctorId).observe(this, result -> {
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                doctor = result.data;
                updateDoctorUI(doctor);
            } else if (result.status == Resource.Status.ERROR) {
                Toast.makeText(this, "Lỗi khi tải thông tin bác sĩ: " + result.message, Toast.LENGTH_SHORT).show();
            }
        });
        
        // Load thông tin dịch vụ
        serviceViewModel.getServiceById(serviceId).observe(this, result -> {
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                service = result.data;
                updateServiceUI(service);
                showLoading(false);
                
                // Tải danh sách slot khả dụng cho ngày hiện tại
                loadTimeSlots();
            } else if (result.status == Resource.Status.ERROR) {
                Toast.makeText(this, "Lỗi khi tải thông tin dịch vụ: " + result.message, Toast.LENGTH_SHORT).show();
                showLoading(false);
            }
        });
    }
    
    private void loadTimeSlots() {
        // TODO: Thực tế cần gọi API để lấy danh sách slot khả dụng
        // Tạm thời tạo danh sách giả để demo
        List<TimeSlot> timeSlots = generateTimeSlots();
        timeSlotAdapter.setTimeSlotList(timeSlots);
    }
    
    private List<TimeSlot> generateTimeSlots() {
        List<TimeSlot> slots = new ArrayList<>();
        
        // Tạo các slot từ 8:00 đến 17:00, mỗi slot cách nhau 30 phút
        String[] times = {
                "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
                "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00"
        };
        
        // Tạo dữ liệu mẫu với một số slot ngẫu nhiên không khả dụng
        for (String time : times) {
            // Tạo một số slot không khả dụng ngẫu nhiên (cách này chỉ để demo)
            boolean isAvailable = Math.random() > 0.3; // 30% slot sẽ không khả dụng
            slots.add(new TimeSlot(time, isAvailable));
        }
        
        return slots;
    }
    
    private void updateDoctorUI(Doctor doctor) {
        // Hiển thị thông tin bác sĩ
        if (doctor.getDoctorNavigation() != null) {
            tvDoctorName.setText(doctor.getDoctorNavigation().getFullName());
            
            // Load ảnh bác sĩ
            if (doctor.getDoctorNavigation().getImage() != null) {
                Glide.with(this)
                        .load(doctor.getDoctorNavigation().getImage())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(ivDoctorImage);
            }
        }
        
        // Hiển thị chuyên khoa của bác sĩ
        if (doctor.getSpecialties() != null && !doctor.getSpecialties().isEmpty()) {
            tvDoctorSpecialty.setText("Chuyên khoa " + doctor.getSpecialties().get(0).getSpecialtyName());
        } else {
            tvDoctorSpecialty.setText("Bác sĩ đa khoa");
        }
    }
    
    private void updateServiceUI(Service service) {
        // Hiển thị thông tin dịch vụ
        tvServiceName.setText(service.getServiceName());
        
        // Format giá tiền
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        tvServicePrice.setText(numberFormat.format(service.getPrice()));
        
        // Hiển thị thời gian
        tvServiceDuration.setText("Thời gian: " + service.getDuration());
    }
    
    private void createReservation() {
        // Kiểm tra thông tin đã đầy đủ chưa
        if (doctor == null || service == null || selectedDate == null || selectedTime == null) {
            Toast.makeText(this, "Vui lòng chọn đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Kiểm tra đăng nhập
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Vui lòng đăng nhập để đặt lịch", Toast.LENGTH_SHORT).show();
            return;
        }
        
        showLoading(true);
        
        // Tạo đối tượng Reservation
        Reservation reservation = new Reservation();
        reservation.setPatientId(sessionManager.getUserId());
        reservation.setDoctorId(doctorId);
        reservation.setServiceId(serviceId);
        
        // Tạo chuỗi ngày giờ hẹn theo định dạng yyyy-MM-dd'T'HH:mm:ss
        String appointmentDate = dateFormat.format(selectedDate) + "T" + selectedTime + ":00";
        reservation.setAppointmentDate(appointmentDate);
        
        // Thiết lập note
        String note = etNote.getText().toString().trim();
        reservation.setNote(note);
        
        // Thiết lập trạng thái ban đầu
        reservation.setStatus("Pending");
        
        // Gọi API đặt lịch
        reservationViewModel.createReservation(reservation).observe(this, result -> {
            showLoading(false);
            
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                // Hiển thị thông báo thành công
                new AlertDialog.Builder(this)
                        .setTitle("Đặt lịch thành công")
                        .setMessage("Lịch khám đã được đặt thành công. Bác sĩ sẽ xác nhận trong thời gian sớm nhất!")
                        .setPositiveButton("Xem lịch hẹn", (dialog, which) -> {
                            // Chuyển đến màn hình lịch hẹn
                            Intent intent = new Intent(BookAppointmentActivity.this, MainActivity.class);
                            intent.putExtra("showAppointments", true);
                            startActivity(intent);
                            finish();
                        })
                        .setCancelable(false)
                        .show();
            } else if (result.status == Resource.Status.ERROR) {
                Toast.makeText(this, "Lỗi khi đặt lịch: " + result.message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        if (isLoading) {
            btnBookAppointment.setEnabled(false);
        } else {
            btnBookAppointment.setEnabled(true);
        }
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
    public void onTimeSlotClick(TimeSlot timeSlot) {
        selectedTime = timeSlot.getTime();
        Toast.makeText(this, "Đã chọn: " + selectedTime, Toast.LENGTH_SHORT).show();
    }
} 