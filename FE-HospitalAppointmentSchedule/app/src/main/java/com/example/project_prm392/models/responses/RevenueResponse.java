package com.example.project_prm392.models.responses;

public class RevenueResponse {
    private double totalRevenue;
    private double monthlyRevenue;
    private double yearlyRevenue;
    private String currency;
    private RevenueHistory[] history;

    public RevenueResponse() {
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getMonthlyRevenue() {
        return monthlyRevenue;
    }

    public void setMonthlyRevenue(double monthlyRevenue) {
        this.monthlyRevenue = monthlyRevenue;
    }

    public double getYearlyRevenue() {
        return yearlyRevenue;
    }

    public void setYearlyRevenue(double yearlyRevenue) {
        this.yearlyRevenue = yearlyRevenue;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public RevenueHistory[] getHistory() {
        return history;
    }

    public void setHistory(RevenueHistory[] history) {
        this.history = history;
    }

    public static class RevenueHistory {
        private String date;
        private double amount;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }
} 