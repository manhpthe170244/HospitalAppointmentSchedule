package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.RatingResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {
    private List<RatingResponse> ratings = new ArrayList<>();

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rating, parent, false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        RatingResponse rating = ratings.get(position);
        holder.bind(rating);
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    public void setRatings(List<RatingResponse> ratings) {
        this.ratings = ratings;
        notifyDataSetChanged();
    }

    static class RatingViewHolder extends RecyclerView.ViewHolder {
        private TextView patientNameTextView;
        private RatingBar ratingBar;
        private TextView commentTextView;
        private TextView dateTextView;

        public RatingViewHolder(@NonNull View itemView) {
            super(itemView);
            patientNameTextView = itemView.findViewById(R.id.patientNameTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }

        public void bind(RatingResponse rating) {
            patientNameTextView.setText(rating.getPatientName());
            ratingBar.setRating(rating.getRating());
            commentTextView.setText(rating.getComment());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dateTextView.setText(sdf.format(rating.getCreatedAt()));
        }
    }
} 