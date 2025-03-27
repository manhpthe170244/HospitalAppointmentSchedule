package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.SlotResponse;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private final List<SlotResponse> timeSlots;
    private final OnTimeSlotSelectedListener listener;
    private int selectedPosition = -1;

    public interface OnTimeSlotSelectedListener {
        void onTimeSlotSelected(SlotResponse timeSlot);
    }

    public TimeSlotAdapter(List<SlotResponse> timeSlots, OnTimeSlotSelectedListener listener) {
        this.timeSlots = timeSlots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_time_slot, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        SlotResponse timeSlot = timeSlots.get(position);
        holder.bind(timeSlot, position);
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        private final MaterialCardView cardView;
        private final TextView tvTimeSlot;

        TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
            tvTimeSlot = itemView.findViewById(R.id.tvTimeSlot);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (selectedPosition != position) {
                        // Uncheck previous selection
                        if (selectedPosition != -1) {
                            notifyItemChanged(selectedPosition);
                        }
                        // Update new selection
                        selectedPosition = position;
                        cardView.setChecked(true);
                        if (listener != null) {
                            listener.onTimeSlotSelected(timeSlots.get(position));
                        }
                    }
                }
            });
        }

        void bind(SlotResponse timeSlot, int position) {
            tvTimeSlot.setText(timeSlot.getSlotTime());
            cardView.setChecked(position == selectedPosition);
            cardView.setEnabled(!timeSlot.isBooked());
            cardView.setAlpha(timeSlot.isBooked() ? 0.5f : 1.0f);
        }
    }
} 