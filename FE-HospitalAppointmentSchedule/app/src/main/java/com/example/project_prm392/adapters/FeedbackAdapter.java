package com.example.project_prm392.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.models.responses.FeedbackResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {
    private List<FeedbackResponse> feedbacks = new ArrayList<>();

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        FeedbackResponse feedback = feedbacks.get(position);
        holder.bind(feedback);
    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }

    public void setFeedbacks(List<FeedbackResponse> feedbacks) {
        this.feedbacks = feedbacks;
        notifyDataSetChanged();
    }

    static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        private TextView patientNameTextView;
        private RatingBar ratingBar;
        private TextView commentTextView;
        private TextView dateTextView;
        private TextView serviceNameTextView;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            patientNameTextView = itemView.findViewById(R.id.patientNameTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            serviceNameTextView = itemView.findViewById(R.id.serviceNameTextView);
        }

        public void bind(FeedbackResponse feedback) {
            patientNameTextView.setText(feedback.getPatientName());
            ratingBar.setRating(feedback.getRating());
            commentTextView.setText(feedback.getComment());
            serviceNameTextView.setText(feedback.getServiceName());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dateTextView.setText(sdf.format(feedback.getCreatedAt()));
        }
    }
} 