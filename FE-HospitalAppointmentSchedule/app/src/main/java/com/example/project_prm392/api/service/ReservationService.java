package com.example.project_prm392.api.service;

import com.example.project_prm392.api.ApiResponse;
import com.example.project_prm392.model.Reservation;
import com.example.project_prm392.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReservationService {
    @GET(Constants.ENDPOINT_RESERVATIONS)
    Call<ApiResponse<List<Reservation>>> getAllReservations();

    @GET(Constants.ENDPOINT_RESERVATIONS + "/patient/{patientId}")
    Call<ApiResponse<List<Reservation>>> getReservationsByPatient(@Path("patientId") int patientId);

    @GET(Constants.ENDPOINT_RESERVATIONS + "/doctor/{doctorId}")
    Call<ApiResponse<List<Reservation>>> getReservationsByDoctor(@Path("doctorId") int doctorId);

    @GET(Constants.ENDPOINT_RESERVATIONS + "/{id}")
    Call<ApiResponse<Reservation>> getReservationById(@Path("id") int reservationId);

    @POST(Constants.ENDPOINT_RESERVATIONS)
    Call<ApiResponse<Reservation>> createReservation(@Body Reservation reservation);

    @PUT(Constants.ENDPOINT_RESERVATIONS + "/{id}")
    Call<ApiResponse<Reservation>> updateReservation(@Path("id") int reservationId, @Body Reservation reservation);

    @PUT(Constants.ENDPOINT_RESERVATIONS + "/{id}/status")
    Call<ApiResponse<Reservation>> updateReservationStatus(@Path("id") int reservationId, @Query("status") String status);

    @DELETE(Constants.ENDPOINT_RESERVATIONS + "/{id}")
    Call<ApiResponse<Void>> deleteReservation(@Path("id") int reservationId);
} 