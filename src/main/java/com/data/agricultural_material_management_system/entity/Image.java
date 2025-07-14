package com.data.agricultural_material_management_system.entity;

import jakarta.persistence.Table;

@Table
public class Image {
    private Long id;
    private String imageUrl;
    private String title;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
} 
