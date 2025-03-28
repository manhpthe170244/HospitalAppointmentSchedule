package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.SpecialtyResponse;

import java.util.List;

public class SpecialtyAdapter extends RecyclerView.Adapter<SpecialtyAdapter.SpecialtyViewHolder> {
    private List<SpecialtyResponse> specialties;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SpecialtyResponse specialty);
    }

    public SpecialtyAdapter(List<SpecialtyResponse> specialties, OnItemClickListener listener) {
        this.specialties = specialties;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SpecialtyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_specialty, parent, false);
        return new SpecialtyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialtyViewHolder holder, int position) {
        SpecialtyResponse specialty = specialties.get(position);
        holder.bind(specialty, listener);
    }

    @Override
    public int getItemCount() {
        return specialties != null ? specialties.size() : 0;
    }

    public void updateData(List<SpecialtyResponse> newSpecialties) {
        this.specialties = newSpecialties;
        notifyDataSetChanged();
    }

    static class SpecialtyViewHolder extends RecyclerView.ViewHolder {
        private TextView specialtyName;
        private TextView specialtyDescription;
        private TextView doctorCount;
        private ImageView specialtyImage;

        public SpecialtyViewHolder(@NonNull View itemView) {
            super(itemView);
            specialtyName = itemView.findViewById(R.id.specialtyName);
            specialtyDescription = itemView.findViewById(R.id.specialtyDescription);
            doctorCount = itemView.findViewById(R.id.doctorCount);
            specialtyImage = itemView.findViewById(R.id.specialtyImage);
        }

        public void bind(SpecialtyResponse specialty, OnItemClickListener listener) {
            specialtyName.setText(specialty.getName());
            specialtyDescription.setText(specialty.getDescription());
            doctorCount.setText(String.format("%d Doctors", specialty.getDoctorCount()));
            // TODO: Load image using Glide or Picasso
            
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(specialty);
                }
            });
        }
    }
} 