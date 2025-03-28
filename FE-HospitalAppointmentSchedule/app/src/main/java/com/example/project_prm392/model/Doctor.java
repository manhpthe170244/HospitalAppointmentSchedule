package com.example.project_prm392.model;

import java.util.List;

public class Doctor {
    private int doctorId;
    private String currentWork;
    private String doctorDescription;
    private String organization;
    private String prize;
    private String researchProject;
    private String trainingProcess;
    private String workExperience;
    private String academicTitle;
    private String degree;
    private List<Certification> certifications;
    private User doctorNavigation;
    private List<DoctorSchedule> doctorSchedules;
    private List<Post> posts;
    private List<Service> services;
    private List<Specialty> specialties;

    // Constructor
    public Doctor() {
    }

    public Doctor(int doctorId, String currentWork, String doctorDescription, String organization,
                 String prize, String researchProject, String trainingProcess, String workExperience,
                 String academicTitle, String degree) {
        this.doctorId = doctorId;
        this.currentWork = currentWork;
        this.doctorDescription = doctorDescription;
        this.organization = organization;
        this.prize = prize;
        this.researchProject = researchProject;
        this.trainingProcess = trainingProcess;
        this.workExperience = workExperience;
        this.academicTitle = academicTitle;
        this.degree = degree;
    }

    // Getters and Setters
    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
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

    public List<Certification> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<Certification> certifications) {
        this.certifications = certifications;
    }

    public User getDoctorNavigation() {
        return doctorNavigation;
    }

    public void setDoctorNavigation(User doctorNavigation) {
        this.doctorNavigation = doctorNavigation;
    }

    public List<DoctorSchedule> getDoctorSchedules() {
        return doctorSchedules;
    }

    public void setDoctorSchedules(List<DoctorSchedule> doctorSchedules) {
        this.doctorSchedules = doctorSchedules;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<Specialty> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<Specialty> specialties) {
        this.specialties = specialties;
    }
} 