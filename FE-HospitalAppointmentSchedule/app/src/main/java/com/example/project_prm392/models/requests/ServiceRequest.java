package com.example.project_prm392.models.requests;

public class ServiceRequest {
    private String name;
    private String description;
    private double price;
    private int specialtyId;
    private String imageUrl;
    private boolean isActive;

    public ServiceRequest(String name, String description, double price, 
                         int specialtyId, String imageUrl, boolean isActive) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.specialtyId = specialtyId;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public int getSpecialtyId() { return specialtyId; }
    public void setSpecialtyId(int specialtyId) { this.specialtyId = specialtyId; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
} 