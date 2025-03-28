package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.MedicalRecordResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MedicalRecordAdapter extends RecyclerView.Adapter<MedicalRecordAdapter.MedicalRecordViewHolder> {
    private List<MedicalRecordResponse> medicalRecords = new ArrayList<>();
    private OnMedicalRecordClickListener listener;

    public interface OnMedicalRecordClickListener {
        void onMedicalRecordClick(MedicalRecordResponse medicalRecord);
    }

    public MedicalRecordAdapter(OnMedicalRecordClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MedicalRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medical_record, parent, false);
        return new MedicalRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalRecordViewHolder holder, int position) {
        MedicalRecordResponse medicalRecord = medicalRecords.get(position);
        holder.bind(medicalRecord);
    }

    @Override
    public int getItemCount() {
        return medicalRecords.size();
    }

    public void setMedicalRecords(List<MedicalRecordResponse> medicalRecords) {
        this.medicalRecords = medicalRecords;
        notifyDataSetChanged();
    }

    class MedicalRecordViewHolder extends RecyclerView.ViewHolder {
        private TextView doctorNameTextView;
        private TextView diagnosisTextView;
        private TextView prescriptionTextView;
        private TextView dateTextView;

        public MedicalRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorNameTextView = itemView.findViewById(R.id.doctorNameTextView);
            diagnosisTextView = itemView.findViewById(R.id.diagnosisTextView);
            prescriptionTextView = itemView.findViewById(R.id.prescriptionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onMedicalRecordClick(medicalRecords.get(position));
                }
            });
        }

        public void bind(MedicalRecordResponse medicalRecord) {
            doctorNameTextView.setText(medicalRecord.getDoctorName());
            diagnosisTextView.setText(medicalRecord.getDiagnosis());
            prescriptionTextView.setText(medicalRecord.getPrescription());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dateTextView.setText(sdf.format(medicalRecord.getCreatedAt()));
        }
    }
} 