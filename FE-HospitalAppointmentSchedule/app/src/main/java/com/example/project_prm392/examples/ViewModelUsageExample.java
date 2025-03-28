package com.example.project_prm392.examples;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm392.R;
import com.example.project_prm392.model.Doctor;
import com.example.project_prm392.utils.Resource;
import com.example.project_prm392.viewmodel.DoctorViewModel;
import com.example.project_prm392.viewmodel.ViewModelFactory;

import java.util.List;

/**
 * Đây là ví dụ về cách sử dụng ViewModelFactory để lấy ViewModel với Dependency Injection
 * Lưu ý: Đây chỉ là ví dụ, không phải để sử dụng trực tiếp
 */
public class ViewModelUsageExample extends AppCompatActivity {

    private DoctorViewModel doctorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Lấy ViewModel sử dụng ViewModelFactory
        // ViewModelFactory sẽ tự động cung cấp các repository cần thiết
        doctorViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance())
                .get(DoctorViewModel.class);
        
        // Sử dụng ViewModel để lấy dữ liệu
        loadDoctors();
    }
    
    private void loadDoctors() {
        // Observer danh sách bác sĩ
        doctorViewModel.getAllDoctors().observe(this, result -> {
            if (result.status == Resource.Status.SUCCESS) {
                // Xử lý dữ liệu khi thành công
                List<Doctor> doctors = result.data;
                // Hiển thị danh sách bác sĩ
                displayDoctors(doctors);
            } else if (result.status == Resource.Status.ERROR) {
                // Xử lý lỗi
                Toast.makeText(this, "Lỗi: " + result.message, Toast.LENGTH_SHORT).show();
            } else if (result.status == Resource.Status.LOADING) {
                // Hiển thị trạng thái loading
                showLoading();
            }
        });
    }
    
    private void displayDoctors(List<Doctor> doctors) {
        // Ví dụ hiển thị danh sách bác sĩ
        // Sử dụng RecyclerView Adapter, v.v.
    }
    
    private void showLoading() {
        // Hiển thị ProgressBar hoặc Shimmer effect, v.v.
    }
} 