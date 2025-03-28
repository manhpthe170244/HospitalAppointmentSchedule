package com.example.project_prm392.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.project_prm392.repository.DoctorRepository;
import com.example.project_prm392.repository.ReservationRepository;
import com.example.project_prm392.repository.ServiceRepository;
import com.example.project_prm392.repository.UserRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private static ViewModelFactory instance;
    
    private final DoctorRepository doctorRepository;
    private final ServiceRepository serviceRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    
    private ViewModelFactory() {
        doctorRepository = new DoctorRepository();
        serviceRepository = new ServiceRepository();
        reservationRepository = new ReservationRepository();
        userRepository = UserRepository.getInstance();
    }
    
    public static ViewModelFactory getInstance() {
        if (instance == null) {
            instance = new ViewModelFactory();
        }
        return instance;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DoctorViewModel.class)) {
            return (T) new DoctorViewModel(doctorRepository);
        } else if (modelClass.isAssignableFrom(ServiceViewModel.class)) {
            return (T) new ServiceViewModel(serviceRepository);
        } else if (modelClass.isAssignableFrom(ReservationViewModel.class)) {
            return (T) new ReservationViewModel(reservationRepository);
        } else if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel(userRepository);
        }
        
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
} 