package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project_prm392.R;
import com.example.project_prm392.databinding.ItemAdminDoctorBinding;
import com.example.project_prm392.models.responses.DoctorResponse;

public class AdminDoctorAdapter extends ListAdapter<DoctorResponse, AdminDoctorAdapter.DoctorViewHolder> {

    private final OnDoctorClickListener listener;

    public AdminDoctorAdapter(OnDoctorClickListener listener) {
        super(new DoctorDiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminDoctorBinding binding = ItemAdminDoctorBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new DoctorViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class DoctorViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdminDoctorBinding binding;

        DoctorViewHolder(ItemAdminDoctorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDoctorClick(getItem(position));
                }
            });

            binding.menuButton.setOnClickListener(this::showPopupMenu);
        }

        void bind(DoctorResponse doctor) {
            binding.doctorName.setText(doctor.getUserName());
            binding.specialtyName.setText(doctor.getSpecialtyName());
            binding.email.setText(doctor.getEmail());

            // Load doctor image
            Glide.with(binding.doctorImage)
                .load(doctor.getAvatarUrl())
                .placeholder(R.drawable.ic_doctor_placeholder)
                .error(R.drawable.ic_doctor_placeholder)
                .circleCrop()
                .into(binding.doctorImage);

            // Set status chip
            binding.statusChip.setText(doctor.isAvailable() ? R.string.available : R.string.unavailable);
            binding.statusChip.setChipBackgroundColorResource(
                doctor.isAvailable() ? R.color.chip_available : R.color.chip_unavailable);
        }

        private void showPopupMenu(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            popup.inflate(R.menu.menu_admin_doctor_item);
            
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                DoctorResponse doctor = getItem(position);
                popup.setOnMenuItemClickListener(item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.action_edit) {
                        listener.onEditClick(doctor);
                        return true;
                    } else if (itemId == R.id.action_delete) {
                        listener.onDeleteClick(doctor);
                        return true;
                    } else if (itemId == R.id.action_schedule) {
                        listener.onScheduleClick(doctor);
                        return true;
                    }
                    return false;
                });
            }
            
            popup.show();
        }
    }

    public interface OnDoctorClickListener {
        void onDoctorClick(DoctorResponse doctor);
        void onEditClick(DoctorResponse doctor);
        void onDeleteClick(DoctorResponse doctor);
        void onScheduleClick(DoctorResponse doctor);
    }

    private static class DoctorDiffCallback extends DiffUtil.ItemCallback<DoctorResponse> {
        @Override
        public boolean areItemsTheSame(@NonNull DoctorResponse oldItem, @NonNull DoctorResponse newItem) {
            return oldItem.getDoctorId() == newItem.getDoctorId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull DoctorResponse oldItem, @NonNull DoctorResponse newItem) {
            return oldItem.getUserName().equals(newItem.getUserName()) &&
                   oldItem.getEmail().equals(newItem.getEmail()) &&
                   oldItem.getSpecialtyName().equals(newItem.getSpecialtyName()) &&
                   oldItem.getAvatarUrl().equals(newItem.getAvatarUrl());
        }
    }
} 