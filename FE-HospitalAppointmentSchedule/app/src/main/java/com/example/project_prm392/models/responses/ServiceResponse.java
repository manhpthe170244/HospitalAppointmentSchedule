package com.example.project_prm392.models.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceResponse {
    @SerializedName("serviceId")
    private int serviceId;
    
    @SerializedName("serviceName")
    private String serviceName;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("overview")
    private String overview;
    
    @SerializedName("price")
    private double price;
    
    @SerializedName("imageUrl")
    private String imageUrl;
    
    @SerializedName("specialtyId")
    private int specialtyId;
    
    @SerializedName("specialtyName")
    private String specialtyName;
    
    @SerializedName("specialties")
    private List<String> specialties;

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public int getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(int specialtyId) {
        this.specialtyId = specialtyId;
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
} 