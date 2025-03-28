package com.example.project_prm392.models.requests;

import java.util.Date;

public class ReservationCreateRequest {
    private int doctorId;
    private int serviceId;
    private Date appointmentDate;
    private String appointmentTime;
    private String reason;

    public ReservationCreateRequest(int doctorId, int serviceId, Date appointmentDate, 
                                  String appointmentTime, String reason) {
        this.doctorId = doctorId;
        this.serviceId = serviceId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.reason = reason;
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

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
} 