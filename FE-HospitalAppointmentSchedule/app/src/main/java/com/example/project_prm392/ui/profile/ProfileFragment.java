package com.example.project_prm392.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.project_prm392.R;
import com.example.project_prm392.model.User;
import com.example.project_prm392.utils.Resource;
import com.example.project_prm392.viewmodel.UserViewModel;
import com.example.project_prm392.viewmodel.ViewModelFactory;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    private CircleImageView ivAvatar;
    private TextView tvChangeAvatar;
    private TextInputEditText etFullName, etEmail, etPhoneNumber, etAddress;
    private Button btnSave, btnChangePassword;
    private ProgressBar progressBar;
    
    private UserViewModel userViewModel;
    private User currentUser;
    
    private static final int REQUEST_GALLERY = 1001;
    private static final int REQUEST_CAMERA = 1002;
    private Uri selectedImageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        // Khởi tạo ViewModel
        userViewModel = new ViewModelProvider(requireActivity(), ViewModelFactory.getInstance())
                .get(UserViewModel.class);
        
        // Khởi tạo Views
        initViews(view);
        
        // Thiết lập listeners
        setupListeners();
        
        // Load dữ liệu người dùng
        loadUserData();
        
        return view;
    }
    
    private void initViews(View view) {
        ivAvatar = view.findViewById(R.id.ivAvatar);
        tvChangeAvatar = view.findViewById(R.id.tvChangeAvatar);
        etFullName = view.findViewById(R.id.etFullName);
        etEmail = view.findViewById(R.id.etEmail);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        etAddress = view.findViewById(R.id.etAddress);
        btnSave = view.findViewById(R.id.btnSave);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        progressBar = view.findViewById(R.id.progressBar);
    }
    
    private void setupListeners() {
        tvChangeAvatar.setOnClickListener(v -> showImagePickerDialog());
        
        btnSave.setOnClickListener(v -> updateProfile());
        
        btnChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ChangePasswordActivity.class);
            startActivity(intent);
        });
    }
    
    private void loadUserData() {
        showLoading(true);
        
        userViewModel.getCurrentUserProfile().observe(getViewLifecycleOwner(), result -> {
            showLoading(false);
            
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                currentUser = result.data;
                updateUI(currentUser);
            } else if (result.status == Resource.Status.ERROR) {
                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void updateUI(User user) {
        etFullName.setText(user.getFullName());
        etEmail.setText(user.getEmail());
        etPhoneNumber.setText(user.getPhoneNumber());
        etAddress.setText(user.getAddress());
        
        if (user.getImage() != null && !user.getImage().isEmpty()) {
            Glide.with(this)
                    .load(user.getImage())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(ivAvatar);
        }
    }
    
    private void updateProfile() {
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        
        if (fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }
        
        showLoading(true);
        
        User updatedUser = new User();
        updatedUser.setUserId(currentUser.getUserId());
        updatedUser.setFullName(fullName);
        updatedUser.setEmail(email);
        updatedUser.setPhoneNumber(phoneNumber);
        updatedUser.setAddress(address);
        updatedUser.setImage(currentUser.getImage()); // Giữ nguyên avatar nếu không thay đổi
        
        userViewModel.updateUser(updatedUser).observe(getViewLifecycleOwner(), result -> {
            showLoading(false);
            
            if (result.status == Resource.Status.SUCCESS) {
                Toast.makeText(requireContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                currentUser = result.data;
            } else if (result.status == Resource.Status.ERROR) {
                Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void showImagePickerDialog() {
        String[] options = {"Máy ảnh", "Thư viện ảnh", "Hủy"};
        
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Chọn ảnh đại diện");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Máy ảnh
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_CAMERA);
                }
            } else if (which == 1) {
                // Thư viện ảnh
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_GALLERY);
            }
        });
        builder.show();
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY && data != null) {
                selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                    ivAvatar.setImageBitmap(bitmap);
                    // TODO: Cần upload ảnh lên server và cập nhật URL vào User
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CAMERA && data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    ivAvatar.setImageBitmap(imageBitmap);
                    // TODO: Cần upload ảnh lên server và cập nhật URL vào User
                }
            }
        }
    }
    
    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        if (isLoading) {
            btnSave.setEnabled(false);
            btnChangePassword.setEnabled(false);
        } else {
            btnSave.setEnabled(true);
            btnChangePassword.setEnabled(true);
        }
    }
} 