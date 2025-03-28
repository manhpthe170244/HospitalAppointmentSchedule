package com.example.project_prm392.model;

public class Device {
    private int deviceId;
    private String deviceName;
    private String description;
    private String image;
    private String status;
    private String lastMaintenanceDate;
    private String nextMaintenanceDate;

    // Constructor
    public Device() {
    }

    public Device(int deviceId, String deviceName, String description, String image,
                 String status, String lastMaintenanceDate, String nextMaintenanceDate) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.description = description;
        this.image = image;
        this.status = status;
        this.lastMaintenanceDate = lastMaintenanceDate;
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    // Getters and Setters
    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(String lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public String getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(String nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
    }
} 