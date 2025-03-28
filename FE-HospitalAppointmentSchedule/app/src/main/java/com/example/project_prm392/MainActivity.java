package com.example.project_prm392;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm392.model.User;
import com.example.project_prm392.ui.auth.LoginActivity;
import com.example.project_prm392.ui.doctor.DoctorListFragment;
import com.example.project_prm392.ui.home.HomeFragment;
import com.example.project_prm392.ui.profile.ProfileFragment;
import com.example.project_prm392.ui.reservation.MyAppointmentsFragment;
import com.example.project_prm392.ui.service.ServiceListFragment;
import com.example.project_prm392.utils.Resource;
import com.example.project_prm392.viewmodel.AuthViewModel;
import com.example.project_prm392.viewmodel.UserViewModel;
import com.example.project_prm392.viewmodel.ViewModelFactory;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    
    private AuthViewModel authViewModel;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Khởi tạo ViewModel
        authViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance())
                .get(AuthViewModel.class);
        userViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance())
                .get(UserViewModel.class);
        
        // Kiểm tra đăng nhập
        if (!authViewModel.isLoggedIn()) {
            navigateToLogin();
            return;
        }
        
        // Ánh xạ views
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        
        // Cập nhật thông tin người dùng trên Navigation Header
        updateUserInfo();
        
        // Mặc định hiển thị fragment Home
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home);
            setTitle("Trang chủ");
        }
    }
    
    private void updateUserInfo() {
        View headerView = navigationView.getHeaderView(0);
        TextView tvUserName = headerView.findViewById(R.id.tvUserName);
        TextView tvUserEmail = headerView.findViewById(R.id.tvUserEmail);
        ImageView ivUserAvatar = headerView.findViewById(R.id.ivUserAvatar);
        
        // Lấy thông tin người dùng từ local storage
        User user = userViewModel.getLocalUser();
        if (user != null) {
            tvUserName.setText(user.getFullName());
            tvUserEmail.setText(user.getEmail());
            // Nếu có avatar, load avatar bằng Glide hoặc Picasso
        }
        
        // Cập nhật thông tin user từ server (sẽ cập nhật UI nếu có thay đổi)
        userViewModel.getCurrentUserProfile().observe(this, result -> {
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                User updatedUser = result.data;
                tvUserName.setText(updatedUser.getFullName());
                tvUserEmail.setText(updatedUser.getEmail());
                // Nếu có avatar, load avatar bằng Glide hoặc Picasso
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        
        if (itemId == R.id.nav_home) {
            switchFragment(new HomeFragment(), "Trang chủ");
        } else if (itemId == R.id.nav_doctors) {
            switchFragment(new DoctorListFragment(), "Danh sách bác sĩ");
        } else if (itemId == R.id.nav_services) {
            switchFragment(new ServiceListFragment(), "Dịch vụ khám");
        } else if (itemId == R.id.nav_appointments) {
            switchFragment(new MyAppointmentsFragment(), "Lịch khám của tôi");
        } else if (itemId == R.id.nav_profile) {
            switchFragment(new ProfileFragment(), "Thông tin cá nhân");
        } else if (itemId == R.id.nav_settings) {
            // TODO: Tạo và mở Settings Fragment
        } else if (itemId == R.id.nav_logout) {
            logout();
        }
        
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    
    private void switchFragment(Fragment fragment, String title) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        
        setTitle(title);
    }
    
    private void logout() {
        authViewModel.logout();
        navigateToLogin();
    }
    
    private void navigateToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}