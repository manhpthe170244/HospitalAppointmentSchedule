package com.example.project_prm392.repository;

import com.example.project_prm392.models.requests.ReservationCreateRequest;
import com.example.project_prm392.models.requests.ReservationUpdateRequest;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.ReservationDetailsResponse;
import com.example.project_prm392.models.responses.ReservationResponse;
import com.example.project_prm392.network.ApiService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;

@Singleton
public class ReservationRepository {
    private final ApiService apiService;

    @Inject
    public ReservationRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Call<BaseResponse<List<ReservationResponse>>> getUserReservations() {
        return apiService.getUserReservations();
    }

    public Call<BaseResponse<ReservationResponse>> getReservationById(int reservationId) {
        return apiService.getReservationById(reservationId);
    }

    public Call<BaseResponse<ReservationDetailsResponse>> getReservationDetails(int reservationId) {
        return apiService.getReservationDetails(reservationId);
    }

    public Call<BaseResponse<List<ReservationResponse>>> getReservationsByPatient(int patientId) {
        return apiService.getReservationsByPatient(patientId);
    }

    public Call<BaseResponse<ReservationResponse>> createReservation(ReservationCreateRequest reservationRequest) {
        return apiService.createReservation(reservationRequest);
    }

    public Call<BaseResponse<ReservationResponse>> updateReservation(int reservationId, ReservationUpdateRequest reservationRequest) {
        return apiService.updateReservation(reservationId, reservationRequest);
    }

    public Call<BaseResponse<Boolean>> cancelReservation(int reservationId, String reason) {
        return apiService.cancelReservation(reservationId, reason);
    }

    public Call<BaseResponse<ReservationResponse>> approveReservation(int reservationId) {
        return apiService.approveReservation(reservationId);
    }

    public Call<BaseResponse<ReservationResponse>> completeReservation(int reservationId) {
        return apiService.completeReservation(reservationId);
    }

    public Call<BaseResponse<Boolean>> checkSlotAvailability(int doctorScheduleId, String appointmentDate) {
        return apiService.checkSlotAvailability(doctorScheduleId, appointmentDate);
    }
} 