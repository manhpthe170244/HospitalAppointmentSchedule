package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.DoctorResponse;

import java.util.ArrayList;
import java.util.List;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.DoctorViewHolder> {
    
    private List<DoctorResponse> doctorsList;
    private List<DoctorResponse> filteredList;
    private OnDoctorClickListener listener;
    
    public interface OnDoctorClickListener {
        void onDoctorClick(int doctorId);
    }
    
    public DoctorsAdapter(List<DoctorResponse> doctorsList) {
        this.doctorsList = doctorsList;
        this.filteredList = new ArrayList<>(doctorsList);
    }
    
    public DoctorsAdapter(List<DoctorResponse> doctorsList, OnDoctorClickListener listener) {
        this.doctorsList = doctorsList;
        this.filteredList = new ArrayList<>(doctorsList);
        this.listener = listener;
    }
    
    public void setFilteredList(List<DoctorResponse> filteredList) {
        this.filteredList = filteredList;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        DoctorResponse doctor = filteredList.get(position);
        holder.bind(doctor);
    }
    
    @Override
    public int getItemCount() {
        return filteredList.size();
    }
    
    class DoctorViewHolder extends RecyclerView.ViewHolder {
        
        private final ImageView imgDoctor;
        private final TextView tvDoctorName;
        private final TextView tvSpecialty;
        private final TextView tvExperience;
        
        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDoctor = itemView.findViewById(R.id.doctorImage);
            tvDoctorName = itemView.findViewById(R.id.doctorName);
            tvSpecialty = itemView.findViewById(R.id.specialization);
            tvExperience = itemView.findViewById(R.id.experience);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDoctorClick(filteredList.get(position).getDoctorId());
                }
            });
        }
        
        public void bind(DoctorResponse doctor) {
            tvDoctorName.setText(doctor.getDoctorName());
            tvSpecialty.setText(doctor.getSpecialtyName() != null ? doctor.getSpecialtyName() : doctor.getSpecialization());
            tvExperience.setText(doctor.getDegree() != null ? doctor.getDegree() : doctor.getExperience());
            
            // Set default doctor avatar if no image URL is provided
            if (doctor.getImageUrl() != null && !doctor.getImageUrl().isEmpty()) {
                Glide.with(imgDoctor.getContext())
                    .load(doctor.getImageUrl())
                    .placeholder(R.drawable.ic_doctor_avatar)
                    .error(R.drawable.ic_doctor_avatar)
                    .circleCrop()
                    .into(imgDoctor);
            } else {
                imgDoctor.setImageResource(R.drawable.ic_doctor_avatar);
            }
        }
    }
} 