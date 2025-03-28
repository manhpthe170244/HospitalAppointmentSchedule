package com.example.project_prm392.model;

public class Comment {
    private int commentId;
    private int feedbackId;
    private int userId;
    private String content;
    private String createdAt;
    private Feedback feedback;
    private User user;

    // Constructor
    public Comment() {
    }

    public Comment(int commentId, int feedbackId, int userId, String content, String createdAt) {
        this.commentId = commentId;
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
} 