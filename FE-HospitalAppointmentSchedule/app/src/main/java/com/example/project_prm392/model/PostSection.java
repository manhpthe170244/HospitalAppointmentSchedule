package com.example.project_prm392.model;

import java.util.List;

public class PostSection {
    private int postSectionId;
    private String sectionName;
    private String description;
    private String image;
    private List<Post> posts;

    // Constructor
    public PostSection() {
    }

    public PostSection(int postSectionId, String sectionName, String description, String image) {
        this.postSectionId = postSectionId;
        this.sectionName = sectionName;
        this.description = description;
        this.image = image;
    }

    // Getters and Setters
    public int getPostSectionId() {
        return postSectionId;
    }

    public void setPostSectionId(int postSectionId) {
        this.postSectionId = postSectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
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

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
} 