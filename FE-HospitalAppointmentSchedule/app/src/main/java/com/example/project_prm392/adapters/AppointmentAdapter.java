package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.ReservationResponse;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<ReservationResponse> appointmentsList;
    private OnAppointmentActionListener listener;

    public interface OnAppointmentActionListener {
        void onRescheduleClick(int position);
        void onCancelClick(int position);
        void onItemClick(int position);
    }

    public AppointmentAdapter(List<ReservationResponse> appointmentsList) {
        this.appointmentsList = appointmentsList;
    }

    public void setAppointments(List<ReservationResponse> appointmentsList) {
        this.appointmentsList = appointmentsList;
        notifyDataSetChanged();
    }

    public void setOnAppointmentActionListener(OnAppointmentActionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        ReservationResponse appointment = appointmentsList.get(position);
        
        holder.tvAppointmentDate.setText(appointment.getAppointmentDate().split("T")[0]);
        holder.tvAppointmentTime.setText(appointment.getSlotTime());
        holder.tvAppointmentStatus.setText(appointment.getStatus());
        
        // Set status text color based on status
        switch (appointment.getStatus().toLowerCase()) {
            case "confirmed":
            case "approved":
                holder.tvAppointmentStatus.setTextColor(0xFF4CAF50); // Green
                break;
            case "pending":
                holder.tvAppointmentStatus.setTextColor(0xFFFF9800); // Orange
                break;
            case "cancelled":
                holder.tvAppointmentStatus.setTextColor(0xFFF44336); // Red
                break;
            case "completed":
                holder.tvAppointmentStatus.setTextColor(0xFF2196F3); // Blue
                break;
        }
        
        holder.tvDoctorName.setText(appointment.getDoctorName());
        holder.tvSpecialty.setText(appointment.getServiceName());
        holder.tvReason.setText(appointment.getReason());
        
        // Only show action buttons for upcoming appointments
        if (appointment.getStatus().equalsIgnoreCase("confirmed") || 
            appointment.getStatus().equalsIgnoreCase("approved") ||
            appointment.getStatus().equalsIgnoreCase("pending")) {
            holder.btnReschedule.setVisibility(View.VISIBLE);
            holder.btnCancelAppointment.setVisibility(View.VISIBLE);
        } else {
            holder.btnReschedule.setVisibility(View.GONE);
            holder.btnCancelAppointment.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return appointmentsList != null ? appointmentsList.size() : 0;
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView tvAppointmentDate, tvAppointmentTime, tvAppointmentStatus;
        TextView tvDoctorName, tvSpecialty, tvReason;
        Button btnReschedule, btnCancelAppointment;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvAppointmentDate = itemView.findViewById(R.id.tvAppointmentDate);
            tvAppointmentTime = itemView.findViewById(R.id.tvAppointmentTime);
            tvAppointmentStatus = itemView.findViewById(R.id.tvAppointmentStatus);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvSpecialty = itemView.findViewById(R.id.tvSpecialty);
            tvReason = itemView.findViewById(R.id.tvReason);
            btnReschedule = itemView.findViewById(R.id.btnReschedule);
            btnCancelAppointment = itemView.findViewById(R.id.btnCancelAppointment);
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
            
            btnReschedule.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onRescheduleClick(position);
                    }
                }
            });
            
            btnCancelAppointment.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onCancelClick(position);
                    }
                }
            });
        }
    }
}