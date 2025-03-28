package com.example.project_prm392.ui.reservation;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.adapter.TimeSlotAdapter;
import com.example.project_prm392.model.Reservation;
import com.example.project_prm392.model.TimeSlot;
import com.example.project_prm392.utils.Constants;
import com.example.project_prm392.utils.Resource;
import com.example.project_prm392.viewmodel.ReservationViewModel;
import com.example.project_prm392.viewmodel.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RescheduleAppointmentActivity extends AppCompatActivity implements TimeSlotAdapter.OnTimeSlotClickListener {
    private TextView tvCurrentDate, tvCurrentTime;
    private CalendarView calendarView;
    private RecyclerView rvTimeSlots;
    private TextInputEditText etReason;
    private Button btnReschedule;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    
    private ReservationViewModel reservationViewModel;
    private TimeSlotAdapter timeSlotAdapter;
    
    private int reservationId;
    private Reservation reservation;
    private Date selectedDate;
    private String selectedTime;
    
    private final SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat outputTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private final SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule_appointment);
        
        // Lấy reservationId từ intent
        reservationId = getIntent().getIntExtra(Constants.EXTRA_RESERVATION_ID, -1);
        if (reservationId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin lịch hẹn", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Khởi tạo ViewModel
        reservationViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance())
                .get(ReservationViewModel.class);
        
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
        loadReservationDetails();
    }
    
    private void initViews() {
        tvCurrentDate = findViewById(R.id.tvCurrentDate);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        calendarView = findViewById(R.id.calendarView);
        rvTimeSlots = findViewById(R.id.rvTimeSlots);
        etReason = findViewById(R.id.etReason);
        btnReschedule = findViewById(R.id.btnReschedule);
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
        btnReschedule.setOnClickListener(v -> rescheduleAppointment());
    }
    
    private void loadReservationDetails() {
        showLoading(true);
        
        reservationViewModel.getReservationById(reservationId).observe(this, result -> {
            showLoading(false);
            
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                reservation = result.data;
                updateCurrentAppointmentUI(reservation);
                
                // Tải danh sách slot khả dụng cho ngày hiện tại
                loadTimeSlots();
            } else if (result.status == Resource.Status.ERROR) {
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void updateCurrentAppointmentUI(Reservation reservation) {
        try {
            // Hiển thị ngày giờ khám hiện tại
            Date appointmentDate = inputDateFormat.parse(reservation.getAppointmentDate());
            if (appointmentDate != null) {
                tvCurrentDate.setText("Ngày: " + outputDateFormat.format(appointmentDate));
                String startTime = outputTimeFormat.format(appointmentDate);
                
                // Tính thời gian kết thúc (giả sử buổi khám kéo dài 30 phút)
                Date endTime = new Date(appointmentDate.getTime() + 30 * 60 * 1000);
                String endTimeStr = outputTimeFormat.format(endTime);
                
                tvCurrentTime.setText("Giờ: " + startTime + " - " + endTimeStr);
            }
        } catch (ParseException e) {
            tvCurrentDate.setText("Ngày: Không xác định");
            tvCurrentTime.setText("Giờ: Không xác định");
        }
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
    
    private void rescheduleAppointment() {
        // Kiểm tra thông tin đã đầy đủ chưa
        if (reservation == null || selectedDate == null || selectedTime == null) {
            Toast.makeText(this, "Vui lòng chọn ngày và giờ mới", Toast.LENGTH_SHORT).show();
            return;
        }
        
        showLoading(true);
        
        // Tạo chuỗi ngày giờ hẹn mới theo định dạng yyyy-MM-dd'T'HH:mm:ss
        String newAppointmentDate = apiDateFormat.format(selectedDate) + "T" + selectedTime + ":00";
        
        // Tạo đối tượng Reservation mới với thông tin cập nhật
        Reservation updatedReservation = new Reservation();
        updatedReservation.setReservationId(reservationId);
        updatedReservation.setAppointmentDate(newAppointmentDate);
        
        // Lấy lý do đổi lịch nếu có
        String reason = etReason.getText().toString().trim();
        updatedReservation.setNote(reason);
        
        // Gọi API đổi lịch
        reservationViewModel.updateReservationDate(updatedReservation).observe(this, result -> {
            showLoading(false);
            
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                // Hiển thị thông báo thành công
                new AlertDialog.Builder(this)
                        .setTitle("Đổi lịch thành công")
                        .setMessage("Lịch khám của bạn đã được đổi thành công!")
                        .setPositiveButton("Đóng", (dialog, which) -> finish())
                        .setCancelable(false)
                        .show();
            } else if (result.status == Resource.Status.ERROR) {
                Toast.makeText(this, "Lỗi khi đổi lịch: " + result.message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        if (isLoading) {
            btnReschedule.setEnabled(false);
        } else {
            btnReschedule.setEnabled(true);
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