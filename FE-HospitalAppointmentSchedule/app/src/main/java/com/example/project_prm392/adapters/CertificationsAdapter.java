package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.CertificationResponse;

import java.util.List;

public class CertificationsAdapter extends RecyclerView.Adapter<CertificationsAdapter.CertificationViewHolder> {
    
    private final List<CertificationResponse> certificationsList;
    
    public CertificationsAdapter(List<CertificationResponse> certificationsList) {
        this.certificationsList = certificationsList;
    }
    
    @NonNull
    @Override
    public CertificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_certification, parent, false);
        return new CertificationViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CertificationViewHolder holder, int position) {
        CertificationResponse certification = certificationsList.get(position);
        holder.bind(certification);
    }
    
    @Override
    public int getItemCount() {
        return certificationsList.size();
    }
    
    static class CertificationViewHolder extends RecyclerView.ViewHolder {
        
        private final ImageView ivCertification;
        private final TextView tvDescription;
        
        public CertificationViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCertification = itemView.findViewById(R.id.ivCertification);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
        
        public void bind(CertificationResponse certification) {
            // Set description if available
            if (certification.getDescription() != null && !certification.getDescription().isEmpty()) {
                tvDescription.setText(certification.getDescription());
                tvDescription.setVisibility(View.VISIBLE);
            } else {
                tvDescription.setVisibility(View.GONE);
            }
            
            // In a real app, we would load the certification image using a library like Glide
            // For now, just show a placeholder
            ivCertification.setImageResource(R.drawable.ic_certification);
        }
    }
} 