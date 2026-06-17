package com.travelcity.controller;

import com.travelcity.dto.ApiResponse;
import com.travelcity.dto.NoteRequest;
import com.travelcity.dto.UpdateNoteRequest;
import com.travelcity.dto.NoteListResponse;
import com.travelcity.dto.NoteResponse;
import com.travelcity.service.NoteService;
import com.travelcity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {
    
    @Autowired
    private NoteService noteService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<NoteListResponse>>> getUserNotes() {
        Long userId = userService.getCurrentUser().getUserId();
        List<NoteListResponse> notes = noteService.getUserNotes(userId);
        return ResponseEntity.ok(ApiResponse.success(notes));
    }
    
    @GetMapping("/{noteId}")
    public ResponseEntity<ApiResponse<NoteResponse>> getNoteById(@PathVariable Long noteId) {
        NoteResponse note = noteService.getNoteById(noteId);
        return ResponseEntity.ok(ApiResponse.success(note));
    }
    
    @GetMapping("/city/{cityId}")
    public ResponseEntity<ApiResponse<NoteResponse>> getNoteByCity(@PathVariable Long cityId) {
        Long userId = userService.getCurrentUser().getUserId();
        NoteResponse note = noteService.getNoteByCity(userId, cityId);
        return ResponseEntity.ok(ApiResponse.success(note));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<NoteResponse>> createNote(@RequestBody NoteRequest request) {
        NoteResponse response = noteService.createNote(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Note created", response));
    }
    
    @PutMapping("/{noteId}")
    public ResponseEntity<ApiResponse<NoteResponse>> updateNote(
            @PathVariable Long noteId,
            @RequestBody UpdateNoteRequest request) {
        NoteResponse response = noteService.updateNote(noteId, request);
        return ResponseEntity.ok(ApiResponse.success("Note updated", response));
    }
    
    @DeleteMapping("/{noteId}")
    public ResponseEntity<ApiResponse<Void>> deleteNote(@PathVariable Long noteId) {
        noteService.deleteNote(noteId);
        return ResponseEntity.ok(ApiResponse.success("Note deleted", null));
    }
}