package com.example.project_prm392.models.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class StatisticsResponse {
    @SerializedName("totalAppointments")
    private int totalAppointments;

    @SerializedName("totalRevenue")
    private double totalRevenue;

    @SerializedName("totalPatients")
    private int totalPatients;

    @SerializedName("appointmentsByStatus")
    private Map<String, Integer> appointmentsByStatus;

    @SerializedName("appointmentsBySpecialty")
    private Map<String, Integer> appointmentsBySpecialty;

    @SerializedName("revenueByMonth")
    private Map<String, Double> revenueByMonth;

    @SerializedName("patientsBySpecialty")
    private Map<String, Integer> patientsBySpecialty;

    @SerializedName("topDoctors")
    private List<DoctorStatsResponse> topDoctors;

    @SerializedName("topServices")
    private List<ServiceStatsResponse> topServices;

    // Getters and Setters
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

    public int getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(int totalPatients) {
        this.totalPatients = totalPatients;
    }

    public Map<String, Integer> getAppointmentsByStatus() {
        return appointmentsByStatus;
    }

    public void setAppointmentsByStatus(Map<String, Integer> appointmentsByStatus) {
        this.appointmentsByStatus = appointmentsByStatus;
    }

    public Map<String, Integer> getAppointmentsBySpecialty() {
        return appointmentsBySpecialty;
    }

    public void setAppointmentsBySpecialty(Map<String, Integer> appointmentsBySpecialty) {
        this.appointmentsBySpecialty = appointmentsBySpecialty;
    }

    public Map<String, Double> getRevenueByMonth() {
        return revenueByMonth;
    }

    public void setRevenueByMonth(Map<String, Double> revenueByMonth) {
        this.revenueByMonth = revenueByMonth;
    }

    public Map<String, Integer> getPatientsBySpecialty() {
        return patientsBySpecialty;
    }

    public void setPatientsBySpecialty(Map<String, Integer> patientsBySpecialty) {
        this.patientsBySpecialty = patientsBySpecialty;
    }

    public List<DoctorStatsResponse> getTopDoctors() {
        return topDoctors;
    }

    public void setTopDoctors(List<DoctorStatsResponse> topDoctors) {
        this.topDoctors = topDoctors;
    }

    public List<ServiceStatsResponse> getTopServices() {
        return topServices;
    }

    public void setTopServices(List<ServiceStatsResponse> topServices) {
        this.topServices = topServices;
    }

    // Inner classes for nested statistics
    public static class DoctorStatsResponse {
        @SerializedName("doctorId")
        private int doctorId;

        @SerializedName("doctorName")
        private String doctorName;

        @SerializedName("appointmentCount")
        private int appointmentCount;

        @SerializedName("revenue")
        private double revenue;

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

        public int getAppointmentCount() {
            return appointmentCount;
        }

        public void setAppointmentCount(int appointmentCount) {
            this.appointmentCount = appointmentCount;
        }

        public double getRevenue() {
            return revenue;
        }

        public void setRevenue(double revenue) {
            this.revenue = revenue;
        }
    }

    public static class ServiceStatsResponse {
        @SerializedName("serviceId")
        private int serviceId;

        @SerializedName("serviceName")
        private String serviceName;

        @SerializedName("count")
        private int count;

        @SerializedName("revenue")
        private double revenue;

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

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getRevenue() {
            return revenue;
        }

        public void setRevenue(double revenue) {
            this.revenue = revenue;
        }
    }
} 