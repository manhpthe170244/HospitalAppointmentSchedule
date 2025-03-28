package com.example.project_prm392.models.responses;

public class DashboardStatsResponse {
    private int totalDoctors;
    private int totalPatients;
    private int totalAppointments;
    private double totalRevenue;
    private String currency;

    public DashboardStatsResponse() {
    }

    public int getTotalDoctors() {
        return totalDoctors;
    }

    public void setTotalDoctors(int totalDoctors) {
        this.totalDoctors = totalDoctors;
    }

    public int getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(int totalPatients) {
        this.totalPatients = totalPatients;
    }

    public int getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(int totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
} 