package com.example.project_prm392.models.requests;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UpdateDoctorRequest {
    @SerializedName("doctorId")
    private int doctorId;

    @SerializedName("userName")
    private String userName;

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

    @SerializedName("specialtyIds")
    private List<String> specialtyIds;

    @SerializedName("isAvailable")
    private boolean isAvailable;

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public List<String> getSpecialtyIds() {
        return specialtyIds;
    }

    public void setSpecialtyIds(List<String> specialtyIds) {
        this.specialtyIds = specialtyIds;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
} 