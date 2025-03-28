package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.DoctorScheduleResponse;

import java.util.List;

public class SchedulesAdapter extends RecyclerView.Adapter<SchedulesAdapter.ScheduleViewHolder> {
    
    private final List<DoctorScheduleResponse> schedulesList;
    private final OnScheduleClickListener listener;
    
    public interface OnScheduleClickListener {
        void onScheduleClick(DoctorScheduleResponse schedule);
    }
    
    public SchedulesAdapter(List<DoctorScheduleResponse> schedulesList, OnScheduleClickListener listener) {
        this.schedulesList = schedulesList;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        DoctorScheduleResponse schedule = schedulesList.get(position);
        holder.bind(schedule);
    }
    
    @Override
    public int getItemCount() {
        return schedulesList.size();
    }
    
    class ScheduleViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDayOfWeek;
        private final TextView tvSlotTime;
        private final TextView tvRoomName;
        private final Button btnBookSlot;
        
        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayOfWeek = itemView.findViewById(R.id.tvDayOfWeek);
            tvSlotTime = itemView.findViewById(R.id.tvSlotTime);
            tvRoomName = itemView.findViewById(R.id.tvRoomName);
            btnBookSlot = itemView.findViewById(R.id.btnBookSlot);
            
            btnBookSlot.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onScheduleClick(schedulesList.get(position));
                }
            });
        }
        
        public void bind(DoctorScheduleResponse schedule) {
            tvDayOfWeek.setText(schedule.getDayOfWeek());
            String timeSlot = String.format("%s - %s", schedule.getStartTime(), schedule.getEndTime());
            tvSlotTime.setText(timeSlot);
            tvRoomName.setText(schedule.getRoomName());
            
            btnBookSlot.setEnabled(schedule.isAvailable());
            btnBookSlot.setAlpha(schedule.isAvailable() ? 1.0f : 0.5f);
        }
    }
} 