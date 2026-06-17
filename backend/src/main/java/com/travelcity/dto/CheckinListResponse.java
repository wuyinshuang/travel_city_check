package com.travelcity.dto;

import java.time.LocalDateTime;

public class CheckinListResponse {
    private Long id;
    private Long cityId;
    private String cityName;
    private Long provinceId;
    private String provinceName;
    private LocalDateTime createdAt;
    
    public CheckinListResponse() {}
    public CheckinListResponse(Long id, Long cityId, String cityName, Long provinceId, String provinceName, LocalDateTime createdAt) {
        this.id = id;
        this.cityId = cityId;
        this.cityName = cityName;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.createdAt = createdAt;
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
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}