package com.travelcity.service;

import com.travelcity.dto.ImageResponse;
import com.travelcity.entity.Image;
import com.travelcity.entity.Note;
import com.travelcity.entity.User;
import com.travelcity.repository.ImageRepository;
import com.travelcity.repository.NoteRepository;
import com.travelcity.config.GlobalExceptionHandler.ForbiddenException;
import com.travelcity.config.GlobalExceptionHandler.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImageService {
    
    @Autowired
    private ImageRepository imageRepository;
    
    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private UserService userService;
    
    @Value("${app.upload.path}")
    private String uploadPath;
    
    @Transactional
    public ImageResponse uploadImage(Long noteId, MultipartFile file) throws IOException {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));

        User user = userService.getCurrentUserEntity();
        if (!note.getUserId().equals(user.getId())) {
            throw new ForbiddenException("Not your note");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String newFilename = UUID.randomUUID().toString() + extension;
        Path filePath = Paths.get(uploadPath, newFilename);

        Files.createDirectories(filePath.getParent());
        Files.copy(file.getInputStream(), filePath);

        Image image = new Image();
        image.setNoteId(noteId);
        image.setFilePath("/uploads/" + newFilename);
        image.setFileName(originalFilename);
        image.setFileSize(file.getSize());

        Image savedImage = imageRepository.save(image);

        return new ImageResponse(
                savedImage.getId(),
                savedImage.getFilePath(),
                savedImage.getFileName(),
                savedImage.getFileSize(),
                savedImage.getCreatedAt()
        );
    }

    @Transactional
    public List<ImageResponse> uploadImages(Long noteId, List<MultipartFile> files) throws IOException {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));

        User user = userService.getCurrentUserEntity();
        if (!note.getUserId().equals(user.getId())) {
            throw new ForbiddenException("Not your note");
        }

        List<ImageResponse> responses = new java.util.ArrayList<>();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String newFilename = UUID.randomUUID().toString() + extension;
            Path filePath = Paths.get(uploadPath, newFilename);

            Files.createDirectories(filePath.getParent());
            Files.copy(file.getInputStream(), filePath);

            Image image = new Image();
            image.setNoteId(noteId);
            image.setFilePath("/uploads/" + newFilename);
            image.setFileName(originalFilename);
            image.setFileSize(file.getSize());

            Image savedImage = imageRepository.save(image);

            responses.add(new ImageResponse(
                    savedImage.getId(),
                    savedImage.getFilePath(),
                    savedImage.getFileName(),
                    savedImage.getFileSize(),
                    savedImage.getCreatedAt()
            ));
        }

        return responses;
    }
    
    @Transactional
    public void deleteImage(Long imageId) {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
        
        User user = userService.getCurrentUserEntity();
        Note note = noteRepository.findById(image.getNoteId())
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        
        if (!note.getUserId().equals(user.getId())) {
            throw new ForbiddenException("Not your image");
        }
        
        try {
            Path filePath = Paths.get(uploadPath, image.getFilePath().substring(9));
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
        }
        
        imageRepository.delete(image);
    }
    
    public List<ImageResponse> getImagesByNote(Long noteId) {
        return imageRepository.findByNoteId(noteId).stream()
                .map(img -> new ImageResponse(
                        img.getId(),
                        img.getFilePath(),
                        img.getFileName(),
                        img.getFileSize(),
                        img.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}