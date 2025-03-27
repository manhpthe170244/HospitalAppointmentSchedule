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
        return certifications.size();
    }

    static class CertificationViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCertificationName;
        private final TextView tvCertificationDate;
        private final TextView tvCertificationIssuer;

        public CertificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCertificationName = itemView.findViewById(R.id.tvCertificationName);
            tvCertificationDate = itemView.findViewById(R.id.tvCertificationDate);
            tvCertificationIssuer = itemView.findViewById(R.id.tvCertificationIssuer);
        }

        public void bind(CertificationResponse certification) {
            tvCertificationName.setText(certification.getCertificationName());
            tvCertificationDate.setText(certification.getIssueDate());
            tvCertificationIssuer.setText(certification.getIssuer());
        }
    }
} 