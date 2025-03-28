package com.example.project_prm392.models.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceResponse {
    @SerializedName("serviceId")
    private int id;
    
    @SerializedName("serviceName")
    private String name;
    
    @SerializedName("description")
    private String description;
    
    @SerializedName("overview")
    private String duration;
    
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

    public ServiceResponse() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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