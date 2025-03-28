package com.example.project_prm392.models.requests;

import java.util.Date;

public class ReservationUpdateRequest {
    private Date appointmentDate;
    private String appointmentTime;
    private String reason;

    public ReservationUpdateRequest(Date appointmentDate, String appointmentTime, String reason) {
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.reason = reason;
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