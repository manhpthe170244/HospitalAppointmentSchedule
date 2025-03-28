package com.example.project_prm392.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.project_prm392.model.Reservation;
import com.example.project_prm392.repository.ReservationRepository;
import com.example.project_prm392.utils.Resource;

import java.util.List;

public class ReservationViewModel extends ViewModel {
    private final ReservationRepository reservationRepository;
    
    // Constructor cho Dependency Injection
    public ReservationViewModel(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }
    
    public LiveData<Resource<List<Reservation>>> getReservationsByPatient() {
        return reservationRepository.getReservationsByPatient();
    }
    
    public LiveData<Resource<Reservation>> getReservationById(int reservationId) {
        return reservationRepository.getReservationById(reservationId);
    }
    
    public LiveData<Resource<Reservation>> createReservation(Reservation reservation) {
        return reservationRepository.createReservation(reservation);
    }
    
    public LiveData<Resource<Reservation>> updateReservation(Reservation reservation) {
        return reservationRepository.updateReservation(reservation);
    }
    
    public LiveData<Resource<Reservation>> updateReservationDate(Reservation reservation) {
        return reservationRepository.updateReservation(reservation);
    }
    
    public LiveData<Resource<Reservation>> updateReservationStatus(int reservationId, String status) {
        return reservationRepository.updateReservationStatus(reservationId, status);
    }
    
    public LiveData<Resource<Void>> cancelReservation(int reservationId) {
        return reservationRepository.cancelReservation(reservationId);
    }
} 