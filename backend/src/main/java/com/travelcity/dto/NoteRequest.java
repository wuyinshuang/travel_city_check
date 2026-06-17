package com.travelcity.dto;

public class NoteRequest {
    private Long cityId;
    private String content;
    
    public NoteRequest() {}
    public NoteRequest(Long cityId, String content) {
        this.cityId = cityId;
        this.content = content;
    }
    
    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}