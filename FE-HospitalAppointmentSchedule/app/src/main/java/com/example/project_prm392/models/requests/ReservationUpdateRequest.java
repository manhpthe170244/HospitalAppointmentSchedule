package com.example.project_prm392.models.requests;

public class ReservationUpdateRequest {
    private int doctorScheduleId;
    private int serviceId;
    private String appointmentDate;
    private String reason;
    private String status;
    private int slotId;

    public ReservationUpdateRequest(int doctorScheduleId, int serviceId, String appointmentDate, String reason, String status, int slotId) {
        this.doctorScheduleId = doctorScheduleId;
        this.serviceId = serviceId;
        this.appointmentDate = appointmentDate;
        this.reason = reason;
        this.status = status;
        this.slotId = slotId;
    }

    public int getDoctorScheduleId() {
        return doctorScheduleId;
    }

    public void setDoctorScheduleId(int doctorScheduleId) {
        this.doctorScheduleId = doctorScheduleId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }
} 