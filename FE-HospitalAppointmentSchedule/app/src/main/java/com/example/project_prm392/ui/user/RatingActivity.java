package com.example.project_prm392.ui.user;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.adapters.RatingAdapter;
import com.example.project_prm392.models.responses.RatingResponse;
import com.example.project_prm392.repository.RatingRepository;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RatingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RatingAdapter adapter;

    @Inject
    RatingRepository ratingRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        // Setup toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Đánh giá");
        }

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);

        // Setup RecyclerView
        adapter = new RatingAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load ratings
        loadRatings();
    }

    private void loadRatings() {
        ratingRepository.getUserRatings().observe(this, response -> {
            if (response.isSuccess() && response.getData() != null) {
                adapter.setRatings(response.getData());
            } else {
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 