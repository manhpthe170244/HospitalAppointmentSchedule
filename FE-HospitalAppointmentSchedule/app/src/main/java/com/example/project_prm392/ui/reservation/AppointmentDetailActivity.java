package com.example.project_prm392.ui.reservation;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.project_prm392.R;
import com.example.project_prm392.model.Reservation;
import com.example.project_prm392.utils.Constants;
import com.example.project_prm392.utils.Resource;
import com.example.project_prm392.viewmodel.ReservationViewModel;
import com.example.project_prm392.viewmodel.ViewModelFactory;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppointmentDetailActivity extends AppCompatActivity {
    private TextView tvAppointmentStatus, tvAppointmentDate, tvAppointmentTime;
    private TextView tvAppointmentId, tvBookingDate;
    private ImageView ivDoctorImage;
    private TextView tvDoctorName, tvDoctorSpecialty;
    private TextView tvServiceName, tvServicePrice, tvServiceDuration;
    private Button btnReschedule, btnCancel;
    private LinearLayout layoutButtons;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    
    private ReservationViewModel reservationViewModel;
    
    private int reservationId;
    private Reservation reservation;
    
    private final SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat outputTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        
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
        
        // Thiết lập listeners
        setupListeners();
        
        // Load dữ liệu
        loadReservationDetails();
    }
    
    private void initViews() {
        tvAppointmentStatus = findViewById(R.id.tvAppointmentStatus);
        tvAppointmentDate = findViewById(R.id.tvAppointmentDate);
        tvAppointmentTime = findViewById(R.id.tvAppointmentTime);
        tvAppointmentId = findViewById(R.id.tvAppointmentId);
        tvBookingDate = findViewById(R.id.tvBookingDate);
        ivDoctorImage = findViewById(R.id.ivDoctorImage);
        tvDoctorName = findViewById(R.id.tvDoctorName);
        tvDoctorSpecialty = findViewById(R.id.tvDoctorSpecialty);
        tvServiceName = findViewById(R.id.tvServiceName);
        tvServicePrice = findViewById(R.id.tvServicePrice);
        tvServiceDuration = findViewById(R.id.tvServiceDuration);
        btnReschedule = findViewById(R.id.btnReschedule);
        btnCancel = findViewById(R.id.btnCancel);
        layoutButtons = findViewById(R.id.layoutButtons);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void setupListeners() {
        btnCancel.setOnClickListener(v -> showCancellationDialog());
        btnReschedule.setOnClickListener(v -> navigateToReschedule());
    }
    
    private void loadReservationDetails() {
        showLoading(true);
        
        reservationViewModel.getReservationById(reservationId).observe(this, result -> {
            showLoading(false);
            
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                reservation = result.data;
                updateUI(reservation);
            } else if (result.status == Resource.Status.ERROR) {
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void updateUI(Reservation reservation) {
        // Hiển thị trạng thái lịch hẹn
        tvAppointmentStatus.setText(getStatusText(reservation.getStatus()));
        setStatusBackground(reservation.getStatus());
        
        // Hiển thị ID lịch hẹn
        tvAppointmentId.setText("APT-" + reservation.getReservationId());
        
        // Hiển thị thời gian đặt lịch
        try {
            // Hiển thị ngày giờ khám
            Date appointmentDate = inputDateFormat.parse(reservation.getAppointmentDate());
            if (appointmentDate != null) {
                tvAppointmentDate.setText(outputDateFormat.format(appointmentDate));
                String startTime = outputTimeFormat.format(appointmentDate);
                
                // Tính thời gian kết thúc (giả sử buổi khám kéo dài 30 phút)
                Date endTime = new Date(appointmentDate.getTime() + 30 * 60 * 1000);
                String endTimeStr = outputTimeFormat.format(endTime);
                
                tvAppointmentTime.setText(startTime + " - " + endTimeStr);
            }
            
            // Hiển thị ngày đặt lịch - giả định là ngày tạo
            Date createdDate = inputDateFormat.parse(reservation.getCreatedAt());
            if (createdDate != null) {
                tvBookingDate.setText(outputDateFormat.format(createdDate));
            }
        } catch (ParseException e) {
            tvAppointmentDate.setText("Không xác định");
            tvAppointmentTime.setText("Không xác định");
            tvBookingDate.setText("Không xác định");
        }
        
        // Hiển thị thông tin bác sĩ
        if (reservation.getDoctor() != null) {
            if (reservation.getDoctor().getDoctorNavigation() != null) {
                tvDoctorName.setText(reservation.getDoctor().getDoctorNavigation().getFullName());
                
                // Load ảnh bác sĩ nếu có
                if (reservation.getDoctor().getDoctorNavigation().getImage() != null) {
                    Glide.with(this)
                            .load(reservation.getDoctor().getDoctorNavigation().getImage())
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .error(R.drawable.ic_launcher_foreground)
                            .into(ivDoctorImage);
                }
            }
            
            // Hiển thị chuyên khoa
            if (reservation.getDoctor().getSpecialties() != null && !reservation.getDoctor().getSpecialties().isEmpty()) {
                tvDoctorSpecialty.setText("Chuyên khoa " + reservation.getDoctor().getSpecialties().get(0).getSpecialtyName());
            } else {
                tvDoctorSpecialty.setText("Bác sĩ đa khoa");
            }
        }
        
        // Hiển thị thông tin dịch vụ
        if (reservation.getService() != null) {
            tvServiceName.setText(reservation.getService().getServiceName());
            
            // Format giá tiền
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvServicePrice.setText(numberFormat.format(reservation.getService().getPrice()));
            
            // Thiết lập thời gian
            tvServiceDuration.setText("Thời gian: " + reservation.getService().getDuration());
        }
        
        // Hiển thị/ẩn buttons dựa vào trạng thái
        boolean canModify = "Pending".equalsIgnoreCase(reservation.getStatus()) || 
                            "Confirmed".equalsIgnoreCase(reservation.getStatus());
        layoutButtons.setVisibility(canModify ? View.VISIBLE : View.GONE);
    }
    
    private String getStatusText(String status) {
        switch (status.toLowerCase()) {
            case "pending":
                return "Chờ xác nhận";
            case "confirmed":
                return "Đã xác nhận";
            case "completed":
                return "Hoàn thành";
            case "cancelled":
                return "Đã hủy";
            default:
                return status;
        }
    }
    
    private void setStatusBackground(String status) {
        int colorResId;
        switch (status.toLowerCase()) {
            case "pending":
                colorResId = R.color.status_pending;
                break;
            case "confirmed":
                colorResId = R.color.status_confirmed;
                break;
            case "completed":
                colorResId = R.color.status_completed;
                break;
            case "cancelled":
                colorResId = R.color.status_cancelled;
                break;
            default:
                colorResId = R.color.purple_500;
                break;
        }
        tvAppointmentStatus.setBackgroundColor(ContextCompat.getColor(this, colorResId));
    }
    
    private void showCancellationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận hủy lịch")
                .setMessage("Bạn có chắc chắn muốn hủy lịch khám này?")
                .setPositiveButton("Hủy lịch", (dialog, which) -> cancelAppointment())
                .setNegativeButton("Không", null)
                .show();
    }
    
    private void cancelAppointment() {
        showLoading(true);
        
        reservationViewModel.updateReservationStatus(reservationId, "Cancelled")
                .observe(this, result -> {
                    showLoading(false);
                    
                    if (result.status == Resource.Status.SUCCESS && result.data != null) {
                        Toast.makeText(this, "Đã hủy lịch hẹn thành công", Toast.LENGTH_SHORT).show();
                        reservation = result.data;
                        updateUI(reservation);
                    } else if (result.status == Resource.Status.ERROR) {
                        Toast.makeText(this, "Lỗi khi hủy lịch: " + result.message, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
    private void navigateToReschedule() {
        Intent intent = new Intent(this, RescheduleAppointmentActivity.class);
        intent.putExtra(Constants.EXTRA_RESERVATION_ID, reservationId);
        startActivity(intent);
    }
    
    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        if (isLoading) {
            btnReschedule.setEnabled(false);
            btnCancel.setEnabled(false);
        } else {
            // Chỉ enable buttons nếu trạng thái lịch hẹn cho phép
            boolean canModify = reservation != null && 
                               ("Pending".equalsIgnoreCase(reservation.getStatus()) || 
                                "Confirmed".equalsIgnoreCase(reservation.getStatus()));
            btnReschedule.setEnabled(canModify);
            btnCancel.setEnabled(canModify);
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
    protected void onResume() {
        super.onResume();
        // Tải lại dữ liệu khi quay lại (có thể đã cập nhật từ màn hình đổi lịch)
        loadReservationDetails();
    }
} 