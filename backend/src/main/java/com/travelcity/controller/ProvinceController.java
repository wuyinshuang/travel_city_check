package com.travelcity.controller;

import com.travelcity.dto.ApiResponse;
import com.travelcity.dto.CityResponse;
import com.travelcity.dto.ProvinceResponse;
import com.travelcity.entity.City;
import com.travelcity.entity.Province;
import com.travelcity.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/provinces")
public class ProvinceController {
    
    @Autowired
    private ProvinceService provinceService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProvinceResponse>>> getAllProvinces() {
        List<ProvinceResponse> provinces = provinceService.getAllProvinces();
        return ResponseEntity.ok(ApiResponse.success(provinces));
    }
    
    @GetMapping("/{provinceId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getProvinceWithCities(@PathVariable Long provinceId) {
        Map<String, Object> data = provinceService.getProvinceWithCities(provinceId);
        return ResponseEntity.ok(ApiResponse.success(data));
    }
    
    @GetMapping("/{provinceId}/map")
    public ResponseEntity<String> getProvinceMapGeoJson(@PathVariable Long provinceId) {
        String geojson = provinceService.getProvinceMapGeoJson(provinceId);
        return ResponseEntity.ok(geojson);
    }

    @GetMapping("/{provinceId}/geojson")
    public ResponseEntity<String> getProvinceGeoJson(@PathVariable Long provinceId) {
        String geojson = provinceService.getProvinceGeoJson(provinceId);
        return ResponseEntity.ok(geojson);
    }
    
    @GetMapping("/{provinceId}/cities")
    public ResponseEntity<ApiResponse<List<CityResponse>>> getCitiesByProvince(@PathVariable Long provinceId) {
        List<CityResponse> cities = provinceService.getCitiesByProvince(provinceId);
        return ResponseEntity.ok(ApiResponse.success(cities));
    }
    
    @GetMapping("/geojson")
    public ResponseEntity<String> getChinaGeoJson() {
        String geojson = provinceService.getChinaGeoJson();
        return ResponseEntity.ok(geojson);
    }

    @GetMapping("/cities/{cityId}")
    public ResponseEntity<ApiResponse<CityResponse>> getCity(@PathVariable Long cityId) {
        City city = provinceService.getCityById(cityId);
        CityResponse response = new CityResponse(city.getId(), city.getName(), city.getCode(), city.getProvinceId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @GetMapping("/cities/{cityId}/geojson")
    public ResponseEntity<String> getCityGeoJson(@PathVariable Long cityId) {
        String geojson = provinceService.getCityGeoJson(cityId);
        return ResponseEntity.ok(geojson);
    }
}