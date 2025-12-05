package com.cloud.cloudnotes.notes;

import com.cloud.cloudnotes.notes.dto.CreateNoteDto;
import com.cloud.cloudnotes.notes.dto.NoteMapper;
import com.cloud.cloudnotes.notes.dto.NoteResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
