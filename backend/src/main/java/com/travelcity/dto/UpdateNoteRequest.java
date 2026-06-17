package com.travelcity.dto;

public class UpdateNoteRequest {
    private String content;
    
    public UpdateNoteRequest() {}
    public UpdateNoteRequest(String content) {
        this.content = content;
    }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}