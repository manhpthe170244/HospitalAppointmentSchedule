package com.example.project_prm392.models.responses;

import java.util.Date;

public class RatingResponse {
    private int id;
    private int doctorId;
    private int userId;
    private int rating;
    private String comment;
    private int reservationId;
    private Date createdAt;
    private Date updatedAt;
    private String patientName;

    public RatingResponse(int id, int doctorId, int userId, int rating,
                         String comment, int reservationId, Date createdAt, Date updatedAt) {
        this.id = id;
        this.doctorId = doctorId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.reservationId = reservationId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
} 