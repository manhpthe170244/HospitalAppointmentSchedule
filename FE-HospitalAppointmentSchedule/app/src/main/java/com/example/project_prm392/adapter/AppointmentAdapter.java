package com.example.project_prm392.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
    private List<Reservation> appointmentList;
    private final Context context;
    private final OnAppointmentActionListener listener;
    private final SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat outputTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public interface OnAppointmentActionListener {
        void onCancelAppointment(Reservation reservation);
        void onRescheduleAppointment(Reservation reservation);
        void onAppointmentClick(Reservation reservation);
    }

    public AppointmentAdapter(Context context, OnAppointmentActionListener listener) {
        this.context = context;
        this.listener = listener;
        this.appointmentList = new ArrayList<>();
    }

    public void setAppointmentList(List<Reservation> appointmentList) {
        this.appointmentList = appointmentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Reservation appointment = appointmentList.get(position);
        
        // Xử lý ngày và giờ
        try {
            Date appointmentDate = inputDateFormat.parse(appointment.getAppointmentDate());
            if (appointmentDate != null) {
                holder.tvAppointmentDate.setText("Ngày " + outputDateFormat.format(appointmentDate));
                holder.tvAppointmentTime.setText(outputTimeFormat.format(appointmentDate) + " - " + 
                        outputTimeFormat.format(new Date(appointmentDate.getTime() + 30 * 60 * 1000))); // Giả sử thời gian khám là 30 phút
            }
        } catch (ParseException e) {
            holder.tvAppointmentDate.setText("Ngày không xác định");
            holder.tvAppointmentTime.setText("Giờ không xác định");
        }
        
        // Thông tin bác sĩ
        if (appointment.getDoctor() != null && appointment.getDoctor().getDoctorNavigation() != null) {
            holder.tvAppointmentDoctor.setText("Bác sĩ: " + appointment.getDoctor().getDoctorNavigation().getFullName());
        } else {
            holder.tvAppointmentDoctor.setText("Bác sĩ: Không xác định");
        }
        
        // Thông tin dịch vụ
        if (appointment.getService() != null) {
            holder.tvAppointmentService.setText("Dịch vụ: " + appointment.getService().getServiceName());
        } else {
            holder.tvAppointmentService.setText("Dịch vụ: Không xác định");
        }
        
        // Trạng thái lịch hẹn
        holder.tvAppointmentStatus.setText(getStatusText(appointment.getStatus()));
        setStatusColor(holder.tvAppointmentStatus, appointment.getStatus());
        
        // Hiện/ẩn nút hủy và đổi lịch dựa vào trạng thái
        boolean isPending = "Pending".equalsIgnoreCase(appointment.getStatus()) || 
                            "Confirmed".equalsIgnoreCase(appointment.getStatus());
        holder.btnCancel.setVisibility(isPending ? View.VISIBLE : View.GONE);
        holder.btnReschedule.setVisibility(isPending ? View.VISIBLE : View.GONE);
        
        // Thiết lập các listener
        holder.btnCancel.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancelAppointment(appointment);
            }
        });
        
        holder.btnReschedule.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRescheduleAppointment(appointment);
            }
        });
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAppointmentClick(appointment);
            }
        });
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

    private void setStatusColor(TextView textView, String status) {
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
        textView.setBackgroundTintList(ContextCompat.getColorStateList(context, colorResId));
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvAppointmentDate, tvAppointmentTime, tvAppointmentDoctor, tvAppointmentService, tvAppointmentStatus;
        Button btnCancel, btnReschedule;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAppointmentDate = itemView.findViewById(R.id.tvAppointmentDate);
            tvAppointmentTime = itemView.findViewById(R.id.tvAppointmentTime);
            tvAppointmentDoctor = itemView.findViewById(R.id.tvAppointmentDoctor);
            tvAppointmentService = itemView.findViewById(R.id.tvAppointmentService);
            tvAppointmentStatus = itemView.findViewById(R.id.tvAppointmentStatus);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            btnReschedule = itemView.findViewById(R.id.btnReschedule);
        }
    }
} 