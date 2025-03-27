package com.example.project_prm392.models.requests;

public class ReservationCreateRequest {
    private int doctorScheduleId;
    private int patientId;
    private int serviceId;
    private String appointmentDate;
    private String reason;
    private int slotId;

    public ReservationCreateRequest(int doctorScheduleId, int patientId, int serviceId, String appointmentDate, String reason, int slotId) {
        this.doctorScheduleId = doctorScheduleId;
        this.patientId = patientId;
        this.serviceId = serviceId;
        this.appointmentDate = appointmentDate;
        this.reason = reason;
        this.slotId = slotId;
    }

    public int getDoctorScheduleId() {
        return doctorScheduleId;
    }

    public void setDoctorScheduleId(int doctorScheduleId) {
        this.doctorScheduleId = doctorScheduleId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
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

    public int getSlotId() {
        return slotId;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }
} 