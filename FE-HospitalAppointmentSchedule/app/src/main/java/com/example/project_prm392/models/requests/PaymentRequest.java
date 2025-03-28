package com.example.project_prm392.models.requests;

public class PaymentRequest {
    private int reservationId;
    private double amount;
    private String paymentMethod;
    private String transactionId;
    private String description;

    public PaymentRequest(int reservationId, double amount, String paymentMethod, 
                         String transactionId, String description) {
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.description = description;
    }

    // Getters and Setters
    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }
    
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
} 