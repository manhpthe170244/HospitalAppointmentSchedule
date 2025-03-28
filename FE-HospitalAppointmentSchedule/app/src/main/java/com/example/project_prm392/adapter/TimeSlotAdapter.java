package com.example.project_prm392.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.model.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {
    private List<TimeSlot> timeSlotList;
    private final Context context;
    private final OnTimeSlotClickListener listener;
    private int selectedPosition = -1;

    public interface OnTimeSlotClickListener {
        void onTimeSlotClick(TimeSlot timeSlot);
    }

    public TimeSlotAdapter(Context context, OnTimeSlotClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.timeSlotList = new ArrayList<>();
    }

    public void setTimeSlotList(List<TimeSlot> timeSlotList) {
        this.timeSlotList = timeSlotList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_time_slot, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        TimeSlot timeSlot = timeSlotList.get(position);
        
        // Thiết lập dữ liệu
        holder.tvTimeSlot.setText(timeSlot.getTime());
        
        // Thiết lập trạng thái của slot (có sẵn hoặc đã đặt)
        if (timeSlot.isAvailable()) {
            holder.cardView.setCardBackgroundColor(
                    position == selectedPosition
                    ? ContextCompat.getColor(context, R.color.colorAccent)
                    : ContextCompat.getColor(context, R.color.white));
            
            holder.tvTimeSlot.setTextColor(
                    position == selectedPosition
                    ? ContextCompat.getColor(context, R.color.white)
                    : ContextCompat.getColor(context, R.color.text_primary));
            
            holder.cardView.setEnabled(true);
        } else {
            // Slot không khả dụng
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.status_cancelled));
            holder.tvTimeSlot.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.cardView.setEnabled(false);
        }
        
        // Thiết lập click listener
        holder.cardView.setOnClickListener(v -> {
            if (timeSlot.isAvailable() && listener != null) {
                int previousSelected = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                
                // Cập nhật giao diện
                notifyItemChanged(previousSelected);
                notifyItemChanged(selectedPosition);
                
                // Thông báo cho activity
                listener.onTimeSlotClick(timeSlot);
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeSlotList.size();
    }

    public static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvTimeSlot;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            tvTimeSlot = itemView.findViewById(R.id.tvTimeSlot);
        }
    }
    
    // Phương thức để lấy vị trí được chọn
    public int getSelectedPosition() {
        return selectedPosition;
    }
    
    // Phương thức để lấy timeslot được chọn
    public TimeSlot getSelectedTimeSlot() {
        if (selectedPosition != -1 && selectedPosition < timeSlotList.size()) {
            return timeSlotList.get(selectedPosition);
        }
        return null;
    }
} 