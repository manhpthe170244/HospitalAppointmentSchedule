package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        private final TextView tvDegree;
        
        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDoctor = itemView.findViewById(R.id.imgDoctor);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvSpecialty = itemView.findViewById(R.id.tvSpecialty);
            tvDegree = itemView.findViewById(R.id.tvDegree);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDoctorClick(filteredList.get(position).getDoctorId());
                }
            });
        }
        
        public void bind(DoctorResponse doctor) {
            tvDoctorName.setText(doctor.getUserName());
            
            // Set specialty
            if (doctor.getSpecialtyName() != null && !doctor.getSpecialtyName().isEmpty()) {
                tvSpecialty.setText(doctor.getSpecialtyName());
                tvSpecialty.setVisibility(View.VISIBLE);
            } else if (doctor.getSpecialties() != null && !doctor.getSpecialties().isEmpty()) {
                tvSpecialty.setText(String.join(", ", doctor.getSpecialties()));
                tvSpecialty.setVisibility(View.VISIBLE);
            } else {
                tvSpecialty.setVisibility(View.GONE);
            }
            
            // Set degree and title
            StringBuilder credentials = new StringBuilder();
            if (doctor.getAcademicTitle() != null && !doctor.getAcademicTitle().isEmpty()) {
                credentials.append(doctor.getAcademicTitle());
            }
            
            if (doctor.getDegree() != null && !doctor.getDegree().isEmpty()) {
                if (credentials.length() > 0) {
                    credentials.append(", ");
                }
                credentials.append(doctor.getDegree());
            }
            
            if (credentials.length() > 0) {
                tvDegree.setText(credentials.toString());
                tvDegree.setVisibility(View.VISIBLE);
            } else {
                tvDegree.setVisibility(View.GONE);
            }
            
            // Set doctor image
            imgDoctor.setImageResource(R.drawable.ic_doctor_avatar);
        }
    }
} 