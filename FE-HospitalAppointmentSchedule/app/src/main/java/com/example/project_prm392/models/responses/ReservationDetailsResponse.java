package com.example.project_prm392.models.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReservationDetailsResponse {

    @SerializedName("reservationId")
    private int reservationId;

    @SerializedName("patientId")
    private int patientId;

    @SerializedName("patientName")
    private String patientName;

    @SerializedName("patientEmail")
    private String patientEmail;

    @SerializedName("patientPhone")
    private String patientPhone;

    @SerializedName("doctorId")
    private int doctorId;

    @SerializedName("doctorName")
    private String doctorName;

    @SerializedName("doctorEmail")
    private String doctorEmail;

    @SerializedName("doctorPhone")
    private String doctorPhone;

    @SerializedName("specialtyId")
    private int specialtyId;

    @SerializedName("specialtyName")
    private String specialtyName;

    @SerializedName("serviceId")
    private int serviceId;

    @SerializedName("serviceName")
    private String serviceName;

    @SerializedName("price")
    private double price;

    @SerializedName("reason")
    private String reason;

    @SerializedName("priorExaminationImg")
    private String priorExaminationImg;

    @SerializedName("appointmentDate")
    private String appointmentDate;

    @SerializedName("status")
    private String status;

    @SerializedName("cancellationReason")
    private String cancellationReason;

    @SerializedName("roomId")
    private int roomId;

    @SerializedName("roomName")
    private String roomName;

    @SerializedName("payments")
    private List<PaymentResponse> payments;

    @SerializedName("medicalRecords")
    private List<MedicalRecordResponse> medicalRecords;

    @SerializedName("feedback")
    private FeedbackResponse feedback;

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

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<PaymentResponse> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentResponse> payments) {
        this.payments = payments;
    }

    public List<MedicalRecordResponse> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecordResponse> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    public FeedbackResponse getFeedback() {
        return feedback;
    }

    public void setFeedback(FeedbackResponse feedback) {
        this.feedback = feedback;
    }
} 