package com.example.project_prm392.model;

import java.util.List;

public class Reservation {
    private int reservationId;
    private int patientId;
    private int doctorId;
    private int serviceId;
    private String reason;
    private String priorExaminationImg;
    private String appointmentDate;
    private String status;
    private String cancellationReason;
    private String updatedDate;
    private String note;
    private Doctor doctor;
    private Patient patient;
    private Service service;
    private String createdAt;
    private Feedback feedback;
    private List<MedicalRecord> medicalRecords;
    private List<Payment> payments;
    private List<DoctorSchedule> doctorSchedules;

    // Constructor
    public Reservation() {
    }

    public Reservation(int reservationId, int patientId, int doctorId, int serviceId, String reason, String priorExaminationImg,
                      String appointmentDate, String status, String cancellationReason, String updatedDate) {
        this.reservationId = reservationId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.serviceId = serviceId;
        this.reason = reason;
        this.priorExaminationImg = priorExaminationImg;
        this.appointmentDate = appointmentDate;
        this.status = status;
        this.cancellationReason = cancellationReason;
        this.updatedDate = updatedDate;
    }

    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPriorExaminationImg() {
        return priorExaminationImg;
    }

    public void setPriorExaminationImg(String priorExaminationImg) {
        this.priorExaminationImg = priorExaminationImg;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<DoctorSchedule> getDoctorSchedules() {
        return doctorSchedules;
    }

    public void setDoctorSchedules(List<DoctorSchedule> doctorSchedules) {
        this.doctorSchedules = doctorSchedules;
    }
} 