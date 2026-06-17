package com.travelcity.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.travelcity.dto.CityResponse;
import com.travelcity.dto.ProvinceResponse;
import com.travelcity.entity.City;
import com.travelcity.entity.Province;
import com.travelcity.repository.CityRepository;
import com.travelcity.repository.ProvinceRepository;
import com.travelcity.config.GlobalExceptionHandler.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProvinceService {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private CityRepository cityRepository;

    public List<ProvinceResponse> getAllProvinces() {
        return provinceRepository.findAll().stream()
                .map(p -> new ProvinceResponse(p.getId(), p.getName(), p.getCode()))
                .collect(Collectors.toList());
    }

    public String getProvinceGeoJson(Long provinceId) {
        Province province = provinceRepository.findById(provinceId)
                .orElseThrow(() -> new ResourceNotFoundException("Province not found"));
        return province.getGeojson();
    }

    public Province getProvinceById(Long provinceId) {
        return provinceRepository.findById(provinceId)
                .orElseThrow(() -> new ResourceNotFoundException("Province not found"));
    }

    public List<CityResponse> getCitiesByProvince(Long provinceId) {
        return cityRepository.findByProvinceId(provinceId).stream()
                .map(c -> new CityResponse(c.getId(), c.getName(), c.getCode(), c.getProvinceId()))
                .collect(Collectors.toList());
    }

    public String getCityGeoJson(Long cityId) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));
        return city.getGeojson();
    }

    public City getCityById(Long cityId) {
        return cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("City not found"));
    }

    public Map<String, Object> getProvinceWithCities(Long provinceId) {
        Province province = getProvinceById(provinceId);
        List<City> cities = cityRepository.findByProvinceId(provinceId);

        return Map.of(
                "province", new ProvinceResponse(province.getId(), province.getName(), province.getCode()),
                "cities", cities.stream()
                        .map(c -> new CityResponse(c.getId(), c.getName(), c.getCode(), c.getProvinceId()))
                        .collect(Collectors.toList())
        );
    }

    public String getProvinceMapGeoJson(Long provinceId) {
        Province province = getProvinceById(provinceId);
        List<City> cities = cityRepository.findByProvinceId(provinceId);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        result.put("type", "FeatureCollection");
        ArrayNode features = result.putArray("features");
        try {
            JsonNode provinceGeoJson = mapper.readTree(province.getGeojson());
            JsonNode provinceFeatures = provinceGeoJson.get("features");
            if (provinceFeatures != null && provinceFeatures.isArray()) {
                for (JsonNode feature : provinceFeatures) {
                    features.add(feature);
                }
            }
        } catch (Exception e) { }
        for (City city : cities) {
            try {
                JsonNode cityGeoJson = mapper.readTree(city.getGeojson());
                features.add(cityGeoJson);
            } catch (Exception e) { }
        }
        return result.toString();
    }

    public String getChinaGeoJson() {
        List<Province> allProvinces = provinceRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode chinaGeoJson = mapper.createObjectNode();
        chinaGeoJson.put("type", "FeatureCollection");
        ArrayNode features = chinaGeoJson.putArray("features");

        for (Province province : allProvinces) {
            try {
                JsonNode provinceGeoJson = mapper.readTree(province.getGeojson());
                JsonNode provinceFeatures = provinceGeoJson.get("features");
                if (provinceFeatures != null && provinceFeatures.isArray()) {
                    for (JsonNode feature : provinceFeatures) {
                        features.add(feature);
                    }
                }
            } catch (Exception e) {
                // skip province features that fail to parse
            }
        }

        return chinaGeoJson.toString();
    }
}