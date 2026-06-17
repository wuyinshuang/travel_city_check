package com.travelcity.dto;

import java.time.LocalDateTime;

public class UserResponse {
    private Long userId;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    
    public UserResponse() {}
    public UserResponse(Long userId, String username, LocalDateTime createdAt, LocalDateTime lastLogin) {
        this.userId = userId;
        this.username = username;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
    }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
}