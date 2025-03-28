 package com.example.project_prm392.model;

public class MedicalRecord {
    private int medicalRecordId;
    private int patientId;
    private int reservationId;
    private String diagnosis;
    private String treatment;
    private String prescription;
    private String notes;
    private String createdAt;
    private Patient patient;
    private Reservation reservation;

    // Constructor
    public MedicalRecord() {
    }

    public MedicalRecord(int medicalRecordId, int patientId, int reservationId, String diagnosis,
                        String treatment, String prescription, String notes, String createdAt) {
        this.medicalRecordId = medicalRecordId;
        this.patientId = patientId;
        this.reservationId = reservationId;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.prescription = prescription;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getMedicalRecordId() {
        return medicalRecordId;
    }

    public void setMedicalRecordId(int medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}