package com.travelcity.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cities")
public class City {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 50)
    private String name;
    
    @Column(name = "province_id", nullable = false)
    private Long provinceId;
    
    @Column(unique = true, nullable = false, length = 10)
    private String code;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String geojson;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", insertable = false, updatable = false)
    private Province province;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Long getProvinceId() { return provinceId; }
    public void setProvinceId(Long provinceId) { this.provinceId = provinceId; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getGeojson() { return geojson; }
    public void setGeojson(String geojson) { this.geojson = geojson; }
    
    public Province getProvince() { return province; }
    public void setProvince(Province province) { this.province = province; }
}