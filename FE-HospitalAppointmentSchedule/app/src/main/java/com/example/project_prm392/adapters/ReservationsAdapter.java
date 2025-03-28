package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.ReservationResponse;
import com.example.project_prm392.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ReservationViewHolder> {
    
    private final List<ReservationResponse> reservationsList;
    private final OnReservationClickListener listener;
    private final SimpleDateFormat inputFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT, Locale.getDefault());
    private final SimpleDateFormat outputDateFormat = new SimpleDateFormat(Constants.DISPLAY_DATE_FORMAT, Locale.getDefault());
    private final SimpleDateFormat outputTimeFormat = new SimpleDateFormat(Constants.DISPLAY_TIME_FORMAT, Locale.getDefault());
    
    public interface OnReservationClickListener {
        void onReservationClick(int reservationId);
    }
    
    public ReservationsAdapter(List<ReservationResponse> reservationsList, OnReservationClickListener listener) {
        this.reservationsList = reservationsList;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        ReservationResponse reservation = reservationsList.get(position);
        holder.bind(reservation);
    }
    
    @Override
    public int getItemCount() {
        return reservationsList.size();
    }
    
    public class ReservationViewHolder extends RecyclerView.ViewHolder {
        
        private final TextView tvDoctorName;
        private final TextView tvSpecialty;
        private final TextView tvDate;
        private final TextView tvTime;
        private final TextView tvStatus;
        private final Button btnDetails;
        
        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvSpecialty = itemView.findViewById(R.id.tvSpecialty);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnDetails = itemView.findViewById(R.id.btnDetails);
            
            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onReservationClick(reservationsList.get(position).getReservationId());
                }
            });
            
            btnDetails.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onReservationClick(reservationsList.get(position).getReservationId());
                }
            });
        }
        
        public void bind(ReservationResponse reservation) {
            tvDoctorName.setText(reservation.getDoctorName());
            tvSpecialty.setText(reservation.getServiceName());
            
            // Format date and time
            try {
                // Kiểm tra xem trường appointmentDate có phải là chuỗi hay không
                if (reservation.getAppointmentDate() != null) {
                    // Nếu là Date, dùng trực tiếp
                    Date appointmentDate = reservation.getAppointmentDate();
                    tvDate.setText(outputDateFormat.format(appointmentDate));
                    tvTime.setText(outputTimeFormat.format(appointmentDate));
                }
            } catch (Exception e) {
                // Nếu có lỗi, hiển thị gốc hoặc thời gian từ timeSlot
                if (reservation.getTimeSlot() != null && !reservation.getTimeSlot().isEmpty()) {
                    tvTime.setText(reservation.getTimeSlot());
                } else {
                    tvTime.setText("");
                }
                tvDate.setText(reservation.getFormattedDate());
            }
            
            // Set status with color
            tvStatus.setText(reservation.getStatus());
            
            int statusColor = switch (reservation.getStatus().toLowerCase()) {
                case "pending" -> R.color.statusPending;
                case "approved" -> R.color.statusApproved;
                case "completed" -> R.color.statusCompleted;
                case "cancelled" -> R.color.statusCancelled;
                default -> R.color.statusPending;
            };
            
            tvStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), statusColor));
        }
    }
} 