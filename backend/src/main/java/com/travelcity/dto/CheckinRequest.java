package com.travelcity.dto;

public class CheckinRequest {
    private Long cityId;
    
    public CheckinRequest() {}
    public CheckinRequest(Long cityId) {
        this.cityId = cityId;
    }
    
    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }
}