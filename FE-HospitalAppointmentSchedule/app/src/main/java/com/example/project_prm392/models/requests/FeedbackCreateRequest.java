package com.example.project_prm392.models.requests;

public class FeedbackCreateRequest {
    private int reservationId;
    private int doctorId;
    private int rating;
    private String comment;

    public FeedbackCreateRequest(int reservationId, int doctorId, int rating, String comment) {
        this.reservationId = reservationId;
        this.doctorId = doctorId;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }
    
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
} 