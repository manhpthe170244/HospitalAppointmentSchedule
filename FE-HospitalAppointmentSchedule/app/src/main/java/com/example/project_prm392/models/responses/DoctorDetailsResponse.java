package com.example.project_prm392.models.responses;

import java.util.List;

public class DoctorDetailsResponse {
    private int doctorId;
    private int userId;
    private String userName;
    private String email;
    private String phone;
    private String gender;
    private String dob;
    private String address;
    private String avatarUrl;
    private String academicTitle;
    private String degree;
    private String currentWork;
    private String doctorDescription;
    private String organization;
    private String prize;
    private String researchProject;
    private String trainingProcess;
    private String workExperience;
    private List<SpecialtyResponse> specialties;
    private List<ServiceResponse> services;
    private List<DoctorScheduleResponse> schedules;

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getCurrentWork() {
        return currentWork;
    }

    public void setCurrentWork(String currentWork) {
        this.currentWork = currentWork;
    }

    public String getDoctorDescription() {
        return doctorDescription;
    }

    public void setDoctorDescription(String doctorDescription) {
        this.doctorDescription = doctorDescription;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getResearchProject() {
        return researchProject;
    }

    public void setResearchProject(String researchProject) {
        this.researchProject = researchProject;
    }

    public String getTrainingProcess() {
        return trainingProcess;
    }

    public void setTrainingProcess(String trainingProcess) {
        this.trainingProcess = trainingProcess;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }

    public List<SpecialtyResponse> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<SpecialtyResponse> specialties) {
        this.specialties = specialties;
    }

    public List<ServiceResponse> getServices() {
        return services;
    }

    public void setServices(List<ServiceResponse> services) {
        this.services = services;
    }

    public List<DoctorScheduleResponse> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<DoctorScheduleResponse> schedules) {
        this.schedules = schedules;
    }
} 