package com.example.project_prm392.models.requests;

public class RatingRequest {
    private Long doctorId;
    private Long patientId;
    private Integer rating;
    private String comment;

    public RatingRequest(Long doctorId, Long patientId, Integer rating, String comment) {
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
} 