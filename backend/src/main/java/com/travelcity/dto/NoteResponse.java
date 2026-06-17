package com.travelcity.dto;

import java.time.LocalDateTime;
import java.util.List;

public class NoteResponse {
    private Long id;
    private Long cityId;
    private String content;
    private List<ImageResponse> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public NoteResponse() {}
    public NoteResponse(Long id, Long cityId, String content, List<ImageResponse> images, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.cityId = cityId;
        this.content = content;
        this.images = images;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public List<ImageResponse> getImages() { return images; }
    public void setImages(List<ImageResponse> images) { this.images = images; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}