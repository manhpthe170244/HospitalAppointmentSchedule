package com.example.project_prm392.adapters;

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
import com.example.project_prm392.models.responses.SlotResponse;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private List<SlotResponse> timeSlots;
    private int selectedPosition = -1;
    private OnTimeSlotSelectedListener listener;

    public interface OnTimeSlotSelectedListener {
        void onTimeSlotSelected(int position);
    }

    public TimeSlotAdapter(List<SlotResponse> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public void setTimeSlots(List<SlotResponse> timeSlots) {
        this.timeSlots = timeSlots;
        notifyDataSetChanged();
    }

    public void setOnTimeSlotSelectedListener(OnTimeSlotSelectedListener listener) {
        this.listener = listener;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public SlotResponse getSelectedTimeSlot() {
        if (selectedPosition != -1 && selectedPosition < timeSlots.size()) {
            return timeSlots.get(selectedPosition);
        }
        return null;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_slot, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        SlotResponse timeSlot = timeSlots.get(position);
        holder.tvTimeSlot.setText(timeSlot.getStartTime() + " - " + timeSlot.getEndTime());
        
        // Set background color based on availability and selection
        if (timeSlot.isAvailable()) {
            if (position == selectedPosition) {
                // Selected slot
                holder.cardTimeSlot.setCardBackgroundColor(0xFF2196F3); // Blue
                holder.tvTimeSlot.setTextColor(0xFFFFFFFF); // White
            } else {
                // Available slot
                holder.cardTimeSlot.setCardBackgroundColor(0xFFFFFFFF); // White
                holder.tvTimeSlot.setTextColor(0xFF000000); // Black
            }
        } else {
            // Unavailable slot
            holder.cardTimeSlot.setCardBackgroundColor(0xFFE0E0E0); // Light Gray
            holder.tvTimeSlot.setTextColor(0xFF9E9E9E); // Gray
        }
    }

    @Override
    public int getItemCount() {
        return timeSlots != null ? timeSlots.size() : 0;
    }

    public class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimeSlot;
        CardView cardTimeSlot;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimeSlot = itemView.findViewById(R.id.tvTimeSlot);
            cardTimeSlot = (CardView) itemView;
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && timeSlots.get(position).isAvailable()) {
                    int oldSelectedPosition = selectedPosition;
                    selectedPosition = position;
                    
                    // Refresh both old and new selected items
                    if (oldSelectedPosition != -1) {
                        notifyItemChanged(oldSelectedPosition);
                    }
                    notifyItemChanged(selectedPosition);
                    
                    if (listener != null) {
                        listener.onTimeSlotSelected(position);
                    }
                }
            });
        }
    }
} 