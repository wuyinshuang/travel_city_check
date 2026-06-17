package com.travelcity.dto;

import java.time.LocalDateTime;
import java.util.List;

public class NoteListResponse {
    private Long id;
    private Long cityId;
    private String cityName;
    private Long provinceId;
    private String provinceName;
    private String content;
    private List<ImageResponse> images;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public NoteListResponse() {}
    public NoteListResponse(Long id, Long cityId, String cityName, Long provinceId, String provinceName, String content, List<ImageResponse> images, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.cityId = cityId;
        this.cityName = cityName;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.content = content;
        this.images = images;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }
    
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
    
    public Long getProvinceId() { return provinceId; }
    public void setProvinceId(Long provinceId) { this.provinceId = provinceId; }
    
    public String getProvinceName() { return provinceName; }
    public void setProvinceName(String provinceName) { this.provinceName = provinceName; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public List<ImageResponse> getImages() { return images; }
    public void setImages(List<ImageResponse> images) { this.images = images; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}