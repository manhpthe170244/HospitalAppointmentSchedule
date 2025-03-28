package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.ServiceResponse;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder> {
    
    private final List<ServiceResponse> servicesList;
    private List<ServiceResponse> filteredList;
    private final OnServiceClickListener listener;
    
    public interface OnServiceClickListener {
        void onServiceClick(int serviceId);
    }
    
    public ServicesAdapter(List<ServiceResponse> servicesList, OnServiceClickListener listener) {
        this.servicesList = servicesList;
        this.filteredList = new ArrayList<>(servicesList);
        this.listener = listener;
    }
    
    public void setFilteredList(List<ServiceResponse> filteredList) {
        this.filteredList = new ArrayList<>(filteredList);
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceResponse service = filteredList.get(position);
        holder.bind(service);
    }
    
    @Override
    public int getItemCount() {
        return filteredList.size();
    }
    
    class ServiceViewHolder extends RecyclerView.ViewHolder {
        
        private final TextView tvServiceName;
        private final TextView tvSpecialty;
        private final TextView tvDescription;
        private final TextView tvPrice;
        private final Button btnDetails;
        
        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvSpecialty = itemView.findViewById(R.id.tvSpecialty);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnDetails = itemView.findViewById(R.id.btnDetails);
        }
        
        public void bind(ServiceResponse service) {
            tvServiceName.setText(service.getName());
            tvSpecialty.setText(service.getSpecialtyName());
            tvDescription.setText(service.getDescription());
            tvPrice.setText(String.format("$%.2f", service.getPrice()));
            
            btnDetails.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onServiceClick(service.getId());
                }
            });
        }
    }
} 