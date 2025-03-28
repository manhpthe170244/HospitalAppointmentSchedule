package com.example.project_prm392.models.requests;

public class RatingRequest {
    private int doctorId;
    private int rating;
    private String comment;
    private int reservationId;

    public RatingRequest(int doctorId, int rating, String comment, int reservationId) {
        this.doctorId = doctorId;
        this.rating = rating;
        this.comment = comment;
        this.reservationId = reservationId;
    }

    // Getters and Setters
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }
} 