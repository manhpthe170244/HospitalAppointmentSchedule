package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.CertificationResponse;

import java.util.List;

public class CertificationsAdapter extends RecyclerView.Adapter<CertificationsAdapter.CertificationViewHolder> {

    private final List<CertificationResponse> certifications;

    public CertificationsAdapter(List<CertificationResponse> certifications) {
        this.certifications = certifications;
    }

    @NonNull
    @Override
    public CertificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_certification, parent, false);
        return new CertificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificationViewHolder holder, int position) {
        CertificationResponse certification = certifications.get(position);
        holder.bind(certification);
    }

    @Override
    public int getItemCount() {
        return certifications != null ? certifications.size() : 0;
    }

    public static class CertificationViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCertificationName;
        private final TextView tvCertificationUrl;
        private final TextView tvDescription;

        public CertificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCertificationName = itemView.findViewById(R.id.tvCertificationName);
            tvCertificationUrl = itemView.findViewById(R.id.tvCertificationUrl);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bind(CertificationResponse certification) {
            // Use certification URL as name if available, otherwise show "Certification #ID"
            String name = certification.getCertificationUrl() != null && !certification.getCertificationUrl().isEmpty()
                    ? certification.getCertificationUrl()
                    : "Certification #" + certification.getCertificationId();
            tvCertificationName.setText(name);
            
            // Show URL if available
            if (certification.getCertificationUrl() != null && !certification.getCertificationUrl().isEmpty()) {
                tvCertificationUrl.setVisibility(View.VISIBLE);
                tvCertificationUrl.setText(certification.getCertificationUrl());
            } else {
                tvCertificationUrl.setVisibility(View.GONE);
            }
            
            // Show description if available
            if (certification.getDescription() != null && !certification.getDescription().isEmpty()) {
                tvDescription.setVisibility(View.VISIBLE);
                tvDescription.setText(certification.getDescription());
            } else {
                tvDescription.setVisibility(View.GONE);
            }
        }
    }
} 