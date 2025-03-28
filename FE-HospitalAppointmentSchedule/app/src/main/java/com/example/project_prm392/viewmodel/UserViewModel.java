package com.example.project_prm392.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_prm392.model.User;
import com.example.project_prm392.repository.UserRepository;
import com.example.project_prm392.utils.Resource;

public class UserViewModel extends ViewModel {
    private final UserRepository userRepository;
    
    // Constructor cho Dependency Injection
    public UserViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public LiveData<Resource<User>> getCurrentUserProfile() {
        return userRepository.getCurrentUserProfile();
    }
    
    public LiveData<Resource<User>> getUserById(int userId) {
        return userRepository.getUserById(userId);
    }
    
    public LiveData<Resource<User>> updateUser(User user) {
        return userRepository.updateUser(user);
    }
    
    public User getLocalUser() {
        return userRepository.getLocalUser();
    }
    
    public LiveData<Resource<Boolean>> changePassword(String currentPassword, String newPassword) {
        return userRepository.changePassword(currentPassword, newPassword);
    }
} 