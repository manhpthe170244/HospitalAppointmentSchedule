package com.example.project_prm392.model;

import java.util.List;

public class Specialty {
    private int specialtyId;
    private String specialtyName;
    private String description;
    private String image;
    private List<Doctor> doctors;
    private List<Service> services;

    // Constructor
    public Specialty() {
    }

    public Specialty(int specialtyId, String specialtyName, String description, String image) {
        this.specialtyId = specialtyId;
        this.specialtyName = specialtyName;
        this.description = description;
        this.image = image;
    }

    // Getters and Setters
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
} 