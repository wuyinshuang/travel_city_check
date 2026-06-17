package com.travelcity.service;

import com.travelcity.dto.NoteRequest;
import com.travelcity.dto.UpdateNoteRequest;
import com.travelcity.dto.ImageResponse;
import com.travelcity.dto.NoteListResponse;
import com.travelcity.dto.NoteResponse;
import com.travelcity.entity.City;
import com.travelcity.entity.Image;
import com.travelcity.entity.Note;
import com.travelcity.entity.User;
import com.travelcity.repository.CityRepository;
import com.travelcity.repository.ImageRepository;
import com.travelcity.repository.NoteRepository;
import com.travelcity.config.GlobalExceptionHandler.ForbiddenException;
import com.travelcity.config.GlobalExceptionHandler.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoteService {
    
    @Autowired
    private NoteRepository noteRepository;
    
    @Autowired
    private ImageRepository imageRepository;
    
    @Autowired
    private CityRepository cityRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProvinceService provinceService;
    
    public List<NoteListResponse> getUserNotes(Long userId) {
        return noteRepository.findByUserId(userId).stream()
                .map(note -> {
                    City city = provinceService.getCityById(note.getCityId());
                    List<ImageResponse> images = imageRepository.findByNoteId(note.getId()).stream()
                            .map(img -> new ImageResponse(
                                    img.getId(),
                                    img.getFilePath(),
                                    img.getFileName(),
                                    img.getFileSize(),
                                    img.getCreatedAt()
                            ))
                            .collect(Collectors.toList());
                    return new NoteListResponse(
                            note.getId(),
                            note.getCityId(),
                            city.getName(),
                            city.getProvinceId(),
                            provinceService.getProvinceById(city.getProvinceId()).getName(),
                            note.getContent(),
                            images,
                            note.getCreatedAt(),
                            note.getUpdatedAt()
                    );
                })
                .collect(Collectors.toList());
    }
    
    public NoteResponse getNoteById(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        
        City city = provinceService.getCityById(note.getCityId());
        List<ImageResponse> images = imageRepository.findByNoteId(noteId).stream()
                .map(img -> new ImageResponse(
                        img.getId(),
                        img.getFilePath(),
                        img.getFileName(),
                        img.getFileSize(),
                        img.getCreatedAt()
                ))
                .collect(Collectors.toList());
        
        return new NoteResponse(
                note.getId(),
                note.getCityId(),
                note.getContent(),
                images,
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }
    
    @Transactional
    public NoteResponse createNote(NoteRequest request) {
        User user = userService.getCurrentUserEntity();
        
        if (noteRepository.existsByUserIdAndCityId(user.getId(), request.getCityId())) {
            throw new IllegalArgumentException("Note already exists for this city");
        }
        
        Note note = new Note();
        note.setUserId(user.getId());
        note.setCityId(request.getCityId());
        note.setContent(request.getContent());
        
        Note savedNote = noteRepository.save(note);
        City city = provinceService.getCityById(request.getCityId());
        
        return new NoteResponse(
                savedNote.getId(),
                savedNote.getCityId(),
                savedNote.getContent(),
                Collections.emptyList(),
                savedNote.getCreatedAt(),
                savedNote.getUpdatedAt()
        );
    }
    
    @Transactional
    public NoteResponse updateNote(Long noteId, UpdateNoteRequest request) {
        User user = userService.getCurrentUserEntity();
        
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        
        if (!note.getUserId().equals(user.getId())) {
            throw new ForbiddenException("Not your note");
        }
        
        note.setContent(request.getContent());
        Note updatedNote = noteRepository.save(note);
        
        List<ImageResponse> images = imageRepository.findByNoteId(noteId).stream()
                .map(img -> new ImageResponse(
                        img.getId(),
                        img.getFilePath(),
                        img.getFileName(),
                        img.getFileSize(),
                        img.getCreatedAt()
                ))
                .collect(Collectors.toList());
        
        return new NoteResponse(
                updatedNote.getId(),
                updatedNote.getCityId(),
                updatedNote.getContent(),
                images,
                updatedNote.getCreatedAt(),
                updatedNote.getUpdatedAt()
        );
    }
    
    @Transactional
    public void deleteNote(Long noteId) {
        User user = userService.getCurrentUserEntity();
        
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        
        if (!note.getUserId().equals(user.getId())) {
            throw new ForbiddenException("Not your note");
        }
        
        imageRepository.deleteByNoteId(noteId);
        noteRepository.delete(note);
    }
    
    public NoteResponse getNoteByCity(Long userId, Long cityId) {
        Note note = noteRepository.findByUserIdAndCityId(userId, cityId)
                .orElse(null);
        
        if (note == null) {
            return null;
        }
        
        List<ImageResponse> images = imageRepository.findByNoteId(note.getId()).stream()
                .map(img -> new ImageResponse(
                        img.getId(),
                        img.getFilePath(),
                        img.getFileName(),
                        img.getFileSize(),
                        img.getCreatedAt()
                ))
                .collect(Collectors.toList());
        
        return new NoteResponse(
                note.getId(),
                note.getCityId(),
                note.getContent(),
                images,
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }
}