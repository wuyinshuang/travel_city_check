package com.travelcity.dto;

public class StatsResponse {
    private Long totalCheckins;
    private Long totalNotes;
    private Long totalImages;
    private Long provincesVisited;
    private Long citiesVisited;
    
    public StatsResponse() {}
    public StatsResponse(Long totalCheckins, Long totalNotes, Long totalImages, Long provincesVisited, Long citiesVisited) {
        this.totalCheckins = totalCheckins;
        this.totalNotes = totalNotes;
        this.totalImages = totalImages;
        this.provincesVisited = provincesVisited;
        this.citiesVisited = citiesVisited;
    }
    
    public Long getTotalCheckins() { return totalCheckins; }
    public void setTotalCheckins(Long totalCheckins) { this.totalCheckins = totalCheckins; }
    
    public Long getTotalNotes() { return totalNotes; }
    public void setTotalNotes(Long totalNotes) { this.totalNotes = totalNotes; }
    
    public Long getTotalImages() { return totalImages; }
    public void setTotalImages(Long totalImages) { this.totalImages = totalImages; }
    
    public Long getProvincesVisited() { return provincesVisited; }
    public void setProvincesVisited(Long provincesVisited) { this.provincesVisited = provincesVisited; }
    
    public Long getCitiesVisited() { return citiesVisited; }
    public void setCitiesVisited(Long citiesVisited) { this.citiesVisited = citiesVisited; }
}