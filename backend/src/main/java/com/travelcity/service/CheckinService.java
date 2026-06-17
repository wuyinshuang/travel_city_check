package com.travelcity.service;

import com.travelcity.dto.CheckinRequest;
import com.travelcity.dto.CheckinListResponse;
import com.travelcity.dto.CheckinResponse;
import com.travelcity.entity.City;
import com.travelcity.entity.Checkin;
import com.travelcity.entity.User;
import com.travelcity.repository.CityRepository;
import com.travelcity.repository.CheckinRepository;
import com.travelcity.config.GlobalExceptionHandler.ForbiddenException;
import com.travelcity.config.GlobalExceptionHandler.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckinService {
    
    @Autowired
    private CheckinRepository checkinRepository;
    
    @Autowired
    private CityRepository cityRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProvinceService provinceService;
    
    public List<CheckinListResponse> getUserCheckins(Long userId) {
        return checkinRepository.findByUserId(userId).stream()
                .map(checkin -> {
                    City city = provinceService.getCityById(checkin.getCityId());
                    return new CheckinListResponse(
                            checkin.getId(),
                            checkin.getCityId(),
                            city.getName(),
                            city.getProvinceId(),
                            provinceService.getProvinceById(city.getProvinceId()).getName(),
                            checkin.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());
    }
    
    public List<CheckinListResponse> getUserCheckinsByProvince(Long userId, Long provinceId) {
        return getUserCheckins(userId).stream()
                .filter(c -> c.getProvinceId().equals(provinceId))
                .collect(Collectors.toList());
    }
    
    @Transactional
    public CheckinResponse createCheckin(CheckinRequest request) {
        User user = userService.getCurrentUserEntity();
        City city = provinceService.getCityById(request.getCityId());
        
        if (checkinRepository.existsByUserIdAndCityId(user.getId(), request.getCityId())) {
            throw new IllegalArgumentException("Already checked in");
        }
        
        Checkin checkin = new Checkin();
        checkin.setUserId(user.getId());
        checkin.setCityId(request.getCityId());
        
        Checkin savedCheckin = checkinRepository.save(checkin);
        
        return new CheckinResponse(
                savedCheckin.getId(),
                savedCheckin.getCityId(),
                city.getName(),
                savedCheckin.getCreatedAt()
        );
    }
    
    @Transactional
    public void deleteCheckin(Long checkinId) {
        User user = userService.getCurrentUserEntity();
        
        Checkin checkin = checkinRepository.findById(checkinId)
                .orElseThrow(() -> new ResourceNotFoundException("Check-in not found"));
        
        if (!checkin.getUserId().equals(user.getId())) {
            throw new ForbiddenException("Not your check-in");
        }
        
        checkinRepository.delete(checkin);
    }
    
    public boolean isCheckedIn(Long userId, Long cityId) {
        return checkinRepository.existsByUserIdAndCityId(userId, cityId);
    }
}