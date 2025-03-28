package com.example.project_prm392.models.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DoctorResponse {
    @SerializedName("doctorId")
    private int doctorId;
    
    @SerializedName("userId")
    private int userId;
    
    @SerializedName("userName")
    private String userName;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("phone")
    private String phone;
    
    @SerializedName("gender")
    private String gender;
    
    @SerializedName("avatarUrl")
    private String avatarUrl;
    
    @SerializedName("academicTitle")
    private String academicTitle;
    
    @SerializedName("degree")
    private String degree;
    
    @SerializedName("currentWork")
    private String currentWork;
    
    @SerializedName("doctorDescription")
    private String doctorDescription;
    
    @SerializedName("specialtyName")
    private String specialtyName;
    
    @SerializedName("specialties")
    private List<String> specialties;

    @SerializedName("id")
    private int id;
    
    @SerializedName("name")
    private String name;
    
    @SerializedName("specialization")
    private String specialization;
    
    @SerializedName("qualification")
    private String qualification;
    
    @SerializedName("experience")
    private String experience;
    
    @SerializedName("imageUrl")
    private String imageUrl;
    
    @SerializedName("isAvailable")
    private boolean isAvailable;
    
    @SerializedName("rating")
    private double rating;
    
    @SerializedName("totalRatings")
    private int totalRatings;

    public DoctorResponse() {
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getCurrentWork() {
        return currentWork;
    }

    public void setCurrentWork(String currentWork) {
        this.currentWork = currentWork;
    }
    
    public String getDoctorDescription() {
        return doctorDescription;
    }

    public void setDoctorDescription(String doctorDescription) {
        this.doctorDescription = doctorDescription;
    }
    
    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    public List<String> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<String> specialties) {
        this.specialties = specialties;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(int totalRatings) {
        this.totalRatings = totalRatings;
    }
} 