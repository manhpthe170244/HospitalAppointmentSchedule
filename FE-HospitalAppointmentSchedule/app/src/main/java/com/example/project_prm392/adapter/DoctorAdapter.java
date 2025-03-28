package com.example.project_prm392.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_prm392.R;
import com.example.project_prm392.model.Doctor;

import java.util.ArrayList;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {
    private List<Doctor> doctorList;
    private final Context context;
    private final OnDoctorClickListener listener;

    public interface OnDoctorClickListener {
        void onDoctorClick(Doctor doctor);
    }

    public DoctorAdapter(Context context, OnDoctorClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.doctorList = new ArrayList<>();
    }

    public void setDoctorList(List<Doctor> doctorList) {
        this.doctorList = doctorList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctorList.get(position);
        
        // Thiết lập dữ liệu
        if (doctor.getDoctorNavigation() != null) {
            holder.tvDoctorName.setText(doctor.getDoctorNavigation().getFullName());
        } else {
            holder.tvDoctorName.setText("Bác sĩ");
        }
        
        // Hiển thị chuyên khoa
        if (doctor.getSpecialties() != null && !doctor.getSpecialties().isEmpty()) {
            holder.tvDoctorSpecialty.setText("Chuyên khoa " + doctor.getSpecialties().get(0).getSpecialtyName());
        } else {
            holder.tvDoctorSpecialty.setText("Chuyên khoa");
        }
        
        // Hiển thị học vị và bằng cấp
        String degreeInfo = doctor.getAcademicTitle();
        if (doctor.getDegree() != null && !doctor.getDegree().isEmpty()) {
            degreeInfo += ", " + doctor.getDegree();
        }
        holder.tvDoctorDegree.setText(degreeInfo);
        
        // Xử lý rating (giả định rating 4.5)
        holder.ratingBar.setRating(4.5f);
        
        // Load ảnh bác sĩ nếu có
        if (doctor.getDoctorNavigation() != null && doctor.getDoctorNavigation().getImage() != null) {
            Glide.with(context)
                    .load(doctor.getDoctorNavigation().getImage())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(holder.ivDoctorImage);
        }
        
        // Thiết lập click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDoctorClick(doctor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public static class DoctorViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDoctorImage;
        TextView tvDoctorName, tvDoctorSpecialty, tvDoctorDegree;
        RatingBar ratingBar;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDoctorImage = itemView.findViewById(R.id.ivDoctorImage);
            tvDoctorName = itemView.findViewById(R.id.tvDoctorName);
            tvDoctorSpecialty = itemView.findViewById(R.id.tvDoctorSpecialty);
            tvDoctorDegree = itemView.findViewById(R.id.tvDoctorDegree);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }
} 