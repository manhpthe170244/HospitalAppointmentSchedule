package com.example.project_prm392.model;

import java.util.List;

public class Feedback {
    private int feedbackId;
    private int reservationId;
    private int rating;
    private String comment;
    private String createdAt;
    private Reservation reservation;
    private List<Comment> comments;

    // Constructor
    public Feedback() {
    }

    public Feedback(int feedbackId, int reservationId, int rating, String comment, String createdAt) {
        this.feedbackId = feedbackId;
        this.reservationId = reservationId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
} 