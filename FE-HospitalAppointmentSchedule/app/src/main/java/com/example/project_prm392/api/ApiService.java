package com.example.project_prm392.api;

import com.example.project_prm392.models.requests.ReservationCreateRequest;
import com.example.project_prm392.models.requests.ReservationUpdateRequest;
import com.example.project_prm392.models.responses.BaseResponse;
import com.example.project_prm392.models.responses.PaginatedResponse;
import com.example.project_prm392.models.responses.ReservationResponse;
import com.example.project_prm392.models.responses.ReservationDetailsResponse;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("reservations/user")
    Call<BaseResponse<PaginatedResponse<ReservationResponse>>> getUserReservations();

    @GET("reservations/{id}")
    Call<BaseResponse<ReservationResponse>> getReservationById(@Path("id") int reservationId);

    @GET("reservations/{id}/details")
    Call<BaseResponse<ReservationDetailsResponse>> getReservationDetails(@Path("id") int reservationId);

    @GET("reservations/patient/{patientId}")
    Call<BaseResponse<List<ReservationResponse>>> getReservationsByPatient(@Path("patientId") int patientId);

    @GET("reservations/date-range")
    Call<BaseResponse<List<ReservationResponse>>> getReservationsByDateRange(
            @Query("startDate") Date startDate,
            @Query("endDate") Date endDate);

    @GET("reservations/status/{status}")
    Call<BaseResponse<List<ReservationResponse>>> getReservationsByStatus(@Path("status") String status);

    @GET("reservations/specialty/{specialtyId}")
    Call<BaseResponse<List<ReservationResponse>>> getReservationsBySpecialty(
            @Path("specialtyId") int specialtyId,
            @Query("date") Date date);

    @POST("reservations")
    Call<BaseResponse<ReservationResponse>> createReservation(@Body ReservationCreateRequest request);

    @PUT("reservations/{id}")
    Call<BaseResponse<ReservationResponse>> updateReservation(
            @Path("id") int reservationId,
            @Body ReservationUpdateRequest request);

    @PUT("reservations/{id}/cancel")
    Call<BaseResponse<ReservationResponse>> cancelReservation(
            @Path("id") int reservationId,
            @Query("reason") String reason);
} 