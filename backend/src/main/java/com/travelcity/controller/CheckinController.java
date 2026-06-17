package com.travelcity.controller;

import com.travelcity.dto.ApiResponse;
import com.travelcity.dto.CheckinRequest;
import com.travelcity.dto.CheckinListResponse;
import com.travelcity.dto.CheckinResponse;
import com.travelcity.service.CheckinService;
import com.travelcity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/checkins")
public class CheckinController {
    
    @Autowired
    private CheckinService checkinService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<CheckinListResponse>>> getUserCheckins() {
        Long userId = userService.getCurrentUser().getUserId();
        List<CheckinListResponse> checkins = checkinService.getUserCheckins(userId);
        return ResponseEntity.ok(ApiResponse.success(checkins));
    }
    
    @GetMapping("/province/{provinceId}")
    public ResponseEntity<ApiResponse<List<CheckinListResponse>>> getUserCheckinsByProvince(
            @PathVariable Long provinceId) {
        Long userId = userService.getCurrentUser().getUserId();
        List<CheckinListResponse> checkins = checkinService.getUserCheckinsByProvince(userId, provinceId);
        return ResponseEntity.ok(ApiResponse.success(checkins));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<CheckinResponse>> createCheckin(@RequestBody CheckinRequest request) {
        CheckinResponse response = checkinService.createCheckin(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Check-in created", response));
    }
    
    @DeleteMapping("/{checkinId}")
    public ResponseEntity<ApiResponse<Void>> deleteCheckin(@PathVariable Long checkinId) {
        checkinService.deleteCheckin(checkinId);
        return ResponseEntity.ok(ApiResponse.success("Check-in deleted", null));
    }
}