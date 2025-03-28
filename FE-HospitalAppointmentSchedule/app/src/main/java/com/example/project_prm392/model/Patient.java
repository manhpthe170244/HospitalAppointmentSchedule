package com.example.project_prm392.model;

import java.util.List;

public class Patient {
    private int patientId;
    private String medicalHistory;
    private String allergies;
    private String bloodType;
    private User patientNavigation;
    private List<Reservation> reservations;
    private List<MedicalRecord> medicalRecords;

    // Constructor
    public Patient() {
    }

    public Patient(int patientId, String medicalHistory, String allergies, String bloodType) {
        this.patientId = patientId;
        this.medicalHistory = medicalHistory;
        this.allergies = allergies;
        this.bloodType = bloodType;
    }

    // Getters and Setters
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public User getPatientNavigation() {
        return patientNavigation;
    }

    public void setPatientNavigation(User patientNavigation) {
        this.patientNavigation = patientNavigation;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }
} 