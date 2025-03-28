package com.example.project_prm392.ui.user;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_prm392.R;
import com.example.project_prm392.adapters.PaymentAdapter;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.PaymentResponse;
import com.example.project_prm392.network.ApiService;
import com.example.project_prm392.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PaymentAdapter adapter;
    private List<PaymentResponse> payments;
    private ApiService apiService;
    private TextView totalAmountText;
    private Button payButton;
    private int reservationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Get reservation ID from intent
        reservationId = getIntent().getIntExtra("reservation_id", -1);
        if (reservationId == -1) {
            Toast.makeText(this, "Invalid reservation", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize views
        recyclerView = findViewById(R.id.paymentHistoryRecyclerView);
        totalAmountText = findViewById(R.id.totalAmountText);
        payButton = findViewById(R.id.payButton);

        // Initialize data
        apiService = RetrofitClient.getInstance().create(ApiService.class);
        payments = new ArrayList<>();

        // Setup RecyclerView
        adapter = new PaymentAdapter(payments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Setup pay button
        payButton.setOnClickListener(v -> processPayment());

        // Load payment history
        loadPaymentHistory();
    }

    private void loadPaymentHistory() {
        apiService.getPaymentHistory(reservationId).enqueue(new Callback<BaseResponse<List<PaymentResponse>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<PaymentResponse>>> call, Response<BaseResponse<List<PaymentResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    payments.clear();
                    payments.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    
                    // Calculate total amount
                    double total = 0;
                    for (PaymentResponse payment : payments) {
                        total += payment.getAmount();
                    }
                    totalAmountText.setText(String.format("Total Amount: $%.2f", total));
                } else {
                    Toast.makeText(PaymentActivity.this, "Failed to load payment history", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<PaymentResponse>>> call, Throwable t) {
                Toast.makeText(PaymentActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processPayment() {
        // TODO: Implement payment processing
        Toast.makeText(this, "Processing payment...", Toast.LENGTH_SHORT).show();
    }
} 