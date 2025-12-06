package com.cloud.cloudnotes.notes;

import com.cloud.cloudnotes.notes.dto.CreateNoteDto;
import com.cloud.cloudnotes.notes.dto.NoteMapper;
import com.cloud.cloudnotes.notes.dto.NoteResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@AllArgsConstructor
public class NoteController {

    private final NoteService noteService;
    private final NoteMapper noteMapper;


    @PostMapping
    public ResponseEntity<NoteResponseDto> createNote(
            @RequestBody CreateNoteDto request
    ) {

        Note note = noteMapper.toEntity(request);
        Note created = noteService.createNote(note);

        return ResponseEntity.ok(noteMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponseDto> getNoteById(@PathVariable String id) {
        return noteService.getNoteById(id)
                .map(note -> ResponseEntity.ok(noteMapper.toResponse(note)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NoteResponseDto>> getAllNotesByUser(@PathVariable String userId) {
        List<NoteResponseDto> dtos = noteService.getAllNotesByUser(userId)
                .stream()
                .map(noteMapper::toResponse)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponseDto> updateNote(
            @PathVariable String id,
            @RequestBody CreateNoteDto request
    ) {
        Note note = noteMapper.toEntity(request);
        return noteService.updateNote(id, note, request.getUserId())
                .map(updated -> ResponseEntity.ok(noteMapper.toResponse(updated)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(
            @PathVariable String id,
            @RequestParam String userId
    ) {
        boolean deleted = noteService.deleteNote(id, userId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
