package com.example.project_prm392.models.responses;

import java.util.List;
import java.util.Map;

public class StatisticsResponse {
    private int totalDoctors;
    private int totalPatients;
    private int totalReservations;
    private int totalCompletedReservations;
    private int totalCancelledReservations;
    private int totalPendingReservations;
    private int totalApprovedReservations;
    private double totalRevenue;
    private List<Map<String, Object>> reservationsByMonth;
    private List<Map<String, Object>> revenueByMonth;
    private List<Map<String, Object>> reservationsBySpecialty;
    private List<Map<String, Object>> reservationsByDoctor;

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

    public int getTotalReservations() {
        return totalReservations;
    }

    public void setTotalReservations(int totalReservations) {
        this.totalReservations = totalReservations;
    }

    public int getTotalCompletedReservations() {
        return totalCompletedReservations;
    }

    public void setTotalCompletedReservations(int totalCompletedReservations) {
        this.totalCompletedReservations = totalCompletedReservations;
    }

    public int getTotalCancelledReservations() {
        return totalCancelledReservations;
    }

    public void setTotalCancelledReservations(int totalCancelledReservations) {
        this.totalCancelledReservations = totalCancelledReservations;
    }

    public int getTotalPendingReservations() {
        return totalPendingReservations;
    }

    public void setTotalPendingReservations(int totalPendingReservations) {
        this.totalPendingReservations = totalPendingReservations;
    }

    public int getTotalApprovedReservations() {
        return totalApprovedReservations;
    }

    public void setTotalApprovedReservations(int totalApprovedReservations) {
        this.totalApprovedReservations = totalApprovedReservations;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public List<Map<String, Object>> getReservationsByMonth() {
        return reservationsByMonth;
    }

    public void setReservationsByMonth(List<Map<String, Object>> reservationsByMonth) {
        this.reservationsByMonth = reservationsByMonth;
    }

    public List<Map<String, Object>> getRevenueByMonth() {
        return revenueByMonth;
    }

    public void setRevenueByMonth(List<Map<String, Object>> revenueByMonth) {
        this.revenueByMonth = revenueByMonth;
    }

    public List<Map<String, Object>> getReservationsBySpecialty() {
        return reservationsBySpecialty;
    }

    public void setReservationsBySpecialty(List<Map<String, Object>> reservationsBySpecialty) {
        this.reservationsBySpecialty = reservationsBySpecialty;
    }

    public List<Map<String, Object>> getReservationsByDoctor() {
        return reservationsByDoctor;
    }

    public void setReservationsByDoctor(List<Map<String, Object>> reservationsByDoctor) {
        this.reservationsByDoctor = reservationsByDoctor;
    }
} 