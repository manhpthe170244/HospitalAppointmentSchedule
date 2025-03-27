package com.example.project_prm392.models.responses;

import com.google.gson.annotations.SerializedName;

public class CertificationResponse {
    
    @SerializedName("certificationId")
    private int certificationId;
    
    @SerializedName("certificationUrl")
    private String certificationUrl;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("doctorId")
    private int doctorId;
    
    // Getters and Setters
    
    public int getCertificationId() {
        return certificationId;
    }
    
    public void setCertificationId(int certificationId) {
        this.certificationId = certificationId;
    }
    
    public String getCertificationUrl() {
        return certificationUrl;
    }
    
    public void setCertificationUrl(String certificationUrl) {
        this.certificationUrl = certificationUrl;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
} 