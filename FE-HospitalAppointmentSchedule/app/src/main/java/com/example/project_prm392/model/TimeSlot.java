package com.example.project_prm392.model;

public class TimeSlot {
    private String time;
    private boolean isAvailable;
    
    public TimeSlot(String time, boolean isAvailable) {
        this.time = time;
        this.isAvailable = isAvailable;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
} 