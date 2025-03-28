package com.example.project_prm392.model;

public class Certification {
    private int certificationId;
    private int doctorId;
    private String certificationName;
    private String issuingOrganization;
    private String issueDate;
    private String expiryDate;
    private String certificateNumber;
    private Doctor doctor;

    // Constructor
    public Certification() {
    }

    public Certification(int certificationId, int doctorId, String certificationName,
                         String issuingOrganization, String issueDate, String expiryDate,
                         String certificateNumber) {
        this.certificationId = certificationId;
        this.doctorId = doctorId;
        this.certificationName = certificationName;
        this.issuingOrganization = issuingOrganization;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.certificateNumber = certificateNumber;
    }

    // Getters and Setters
    public int getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(int certificationId) {
        this.certificationId = certificationId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getCertificationName() {
        return certificationName;
    }

    public void setCertificationName(String certificationName) {
        this.certificationName = certificationName;
    }

    public String getIssuingOrganization() {
        return issuingOrganization;
    }

    public void setIssuingOrganization(String issuingOrganization) {
        this.issuingOrganization = issuingOrganization;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
} 