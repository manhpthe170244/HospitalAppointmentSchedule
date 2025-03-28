package com.example.project_prm392.model;

public class Post {
    private int postId;
    private int doctorId;
    private int postSectionId;
    private String title;
    private String content;
    private String image;
    private String createdAt;
    private String updatedAt;
    private Doctor doctor;
    private PostSection postSection;

    // Constructor
    public Post() {
    }

    public Post(int postId, int doctorId, int postSectionId, String title, String content,
                String image, String createdAt, String updatedAt) {
        this.postId = postId;
        this.doctorId = doctorId;
        this.postSectionId = postSectionId;
        this.title = title;
        this.content = content;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getPostSectionId() {
        return postSectionId;
    }

    public void setPostSectionId(int postSectionId) {
        this.postSectionId = postSectionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public PostSection getPostSection() {
        return postSection;
    }

    public void setPostSection(PostSection postSection) {
        this.postSection = postSection;
    }
} 