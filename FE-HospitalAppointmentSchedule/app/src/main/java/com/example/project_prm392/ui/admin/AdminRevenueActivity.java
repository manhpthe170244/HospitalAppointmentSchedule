package com.example.project_prm392.ui.admin;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.adapters.RevenueAdapter;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.RevenueResponse;
import com.example.project_prm392.network.ApiService;
import com.example.project_prm392.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminRevenueActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RevenueAdapter adapter;
    private List<RevenueResponse> revenues;
    private ApiService apiService;
    private TextView totalRevenueText;
    private TextView monthlyRevenueText;
    private TextView yearlyRevenueText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_revenue);

        // Initialize views
        recyclerView = findViewById(R.id.revenueRecyclerView);
        totalRevenueText = findViewById(R.id.totalRevenueText);
        monthlyRevenueText = findViewById(R.id.monthlyRevenueText);
        yearlyRevenueText = findViewById(R.id.yearlyRevenueText);

        // Initialize data
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        revenues = new ArrayList<>();

        // Setup RecyclerView
        adapter = new RevenueAdapter(revenues);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Load revenue data
        loadRevenueData();
    }

    private void loadRevenueData() {
        apiService.getRevenueStats().enqueue(new Callback<BaseResponse<RevenueResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<RevenueResponse>> call, Response<BaseResponse<RevenueResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RevenueResponse revenue = response.body().getData();
                    totalRevenueText.setText(String.format("$%.2f", revenue.getTotalRevenue()));
                    monthlyRevenueText.setText(String.format("$%.2f", revenue.getMonthlyRevenue()));
                    yearlyRevenueText.setText(String.format("$%.2f", revenue.getYearlyRevenue()));
                    
                    if (revenue.getRevenueHistory() != null) {
                        revenues.clear();
                        revenues.addAll(revenue.getRevenueHistory());
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(AdminRevenueActivity.this, "Failed to load revenue data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<RevenueResponse>> call, Throwable t) {
                Toast.makeText(AdminRevenueActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
} 