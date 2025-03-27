package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.SpecialtyResponse;

import java.util.List;

public class SpecialtiesAdapter extends RecyclerView.Adapter<SpecialtiesAdapter.SpecialtyViewHolder> {
    
    private final List<SpecialtyResponse> specialtiesList;
    private OnSpecialtyClickListener listener;
    
    public interface OnSpecialtyClickListener {
        void onSpecialtyClick(int specialtyId);
    }
    
    public SpecialtiesAdapter(List<SpecialtyResponse> specialtiesList) {
        this.specialtiesList = specialtiesList;
    }
    
    public SpecialtiesAdapter(List<SpecialtyResponse> specialtiesList, OnSpecialtyClickListener listener) {
        this.specialtiesList = specialtiesList;
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
        SpecialtyResponse specialty = specialtiesList.get(position);
        holder.bind(specialty);
    }
    
    @Override
    public int getItemCount() {
        return specialtiesList.size();
    }
    
    class SpecialtyViewHolder extends RecyclerView.ViewHolder {
        
        private final TextView tvSpecialtyName;
        
        public SpecialtyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSpecialtyName = itemView.findViewById(R.id.tvSpecialtyName);
            
            if (listener != null) {
                itemView.setOnClickListener(v -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onSpecialtyClick(specialtiesList.get(position).getSpecialtyId());
                    }
                });
            }
        }
        
        public void bind(SpecialtyResponse specialty) {
            tvSpecialtyName.setText(specialty.getSpecialtyName());
        }
    }
} 