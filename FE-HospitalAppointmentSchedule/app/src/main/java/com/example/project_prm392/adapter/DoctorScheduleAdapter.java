package com.example.project_prm392.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.model.DoctorSchedule;

import java.util.ArrayList;
import java.util.List;

public class DoctorScheduleAdapter extends RecyclerView.Adapter<DoctorScheduleAdapter.ScheduleViewHolder> {
    private List<DoctorSchedule> scheduleList;
    private final Context context;

    public DoctorScheduleAdapter(Context context) {
        this.context = context;
        this.scheduleList = new ArrayList<>();
    }

    public void setScheduleList(List<DoctorSchedule> scheduleList) {
        this.scheduleList = scheduleList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_doctor_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        DoctorSchedule schedule = scheduleList.get(position);
        
        // Thiết lập dữ liệu
        holder.tvDayOfWeek.setText(translateDayOfWeek(schedule.getDayOfWeek()));
        holder.tvTime.setText(schedule.getStartTime() + " - " + schedule.getEndTime());
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }
    
    // Hàm chuyển đổi từ tiếng Anh sang tiếng Việt cho ngày trong tuần
    private String translateDayOfWeek(String dayOfWeek) {
        switch (dayOfWeek.toLowerCase()) {
            case "monday":
                return "Thứ Hai";
            case "tuesday":
                return "Thứ Ba";
            case "wednesday":
                return "Thứ Tư";
            case "thursday":
                return "Thứ Năm";
            case "friday":
                return "Thứ Sáu";
            case "saturday":
                return "Thứ Bảy";
            case "sunday":
                return "Chủ Nhật";
            default:
                return dayOfWeek;
        }
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayOfWeek, tvTime;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayOfWeek = itemView.findViewById(R.id.tvDayOfWeek);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
} 