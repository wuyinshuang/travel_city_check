package com.travelcity.dto;

import java.time.LocalDateTime;

public class ImageResponse {
    private Long id;
    private String filePath;
    private String fileName;
    private Long fileSize;
    private LocalDateTime createdAt;
    
    public ImageResponse() {}
    public ImageResponse(Long id, String filePath, String fileName, Long fileSize, LocalDateTime createdAt) {
        this.id = id;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.createdAt = createdAt;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}