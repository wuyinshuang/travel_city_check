package com.travelcity.controller;

import com.travelcity.dto.ApiResponse;
import com.travelcity.dto.ImageResponse;
import com.travelcity.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {
    
    @Autowired
    private ImageService imageService;
    
    @PostMapping("/note/{noteId}")
    public ResponseEntity<ApiResponse<ImageResponse>> uploadImage(
            @PathVariable Long noteId,
            @RequestParam("file") MultipartFile file) throws IOException {
        ImageResponse response = imageService.uploadImage(noteId, file);
        return ResponseEntity.ok(ApiResponse.success("Image uploaded", response));
    }

    @PostMapping("/note/{noteId}/batch")
    public ResponseEntity<ApiResponse<List<ImageResponse>>> uploadImages(
            @PathVariable Long noteId,
            @RequestPart("files") List<MultipartFile> files) throws IOException {
        List<ImageResponse> responses = imageService.uploadImages(noteId, files);
        return ResponseEntity.ok(ApiResponse.success("Images uploaded", responses));
    }
    
    @DeleteMapping("/{imageId}")
    public ResponseEntity<ApiResponse<Void>> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.ok(ApiResponse.success("Image deleted", null));
    }
    
    @GetMapping("/note/{noteId}")
    public ResponseEntity<ApiResponse<List<ImageResponse>>> getImagesByNote(@PathVariable Long noteId) {
        List<ImageResponse> images = imageService.getImagesByNote(noteId);
        return ResponseEntity.ok(ApiResponse.success(images));
    }
}