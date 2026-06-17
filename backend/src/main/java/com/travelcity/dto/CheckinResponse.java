package com.travelcity.dto;

import java.time.LocalDateTime;

public class CheckinResponse {
    private Long id;
    private Long cityId;
    private String cityName;
    private LocalDateTime createdAt;
    
    public CheckinResponse() {}
    public CheckinResponse(Long id, Long cityId, String cityName, LocalDateTime createdAt) {
        this.id = id;
        this.cityId = cityId;
        this.cityName = cityName;
        this.createdAt = createdAt;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }
    
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}