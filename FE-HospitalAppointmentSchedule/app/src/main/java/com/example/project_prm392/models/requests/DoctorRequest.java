package com.example.project_prm392.models.requests;

import java.util.List;

public class DoctorRequest {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String description;
    private List<Integer> specialtyIds;
    private String imageUrl;
    private boolean isActive;

    public DoctorRequest(String name, String email, String phone, String address, 
                        String description, List<Integer> specialtyIds, String imageUrl, boolean isActive) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.description = description;
        this.specialtyIds = specialtyIds;
        this.imageUrl = imageUrl;
        this.isActive = isActive;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<Integer> getSpecialtyIds() { return specialtyIds; }
    public void setSpecialtyIds(List<Integer> specialtyIds) { this.specialtyIds = specialtyIds; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
} 