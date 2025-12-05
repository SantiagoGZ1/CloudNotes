package com.cloud.cloudnotes.notes.dto;

import com.cloud.cloudnotes.notes.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteMapper {

    public Note toEntity(CreateNoteDto dto) {
        if (dto == null) {
            return null;
        }
        Note note = new Note();
        note.setUserId(dto.getUserId());
        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());
        return note;
    }

    public NoteResponseDto toResponse(Note note) {
        if (note == null) {
            return null;
        }
        NoteResponseDto dto = new NoteResponseDto();
        dto.setNoteId(note.getNoteId());
        dto.setUserId(note.getUserId());
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());
        dto.setCreatedAt(note.getCreatedAt());
        dto.setUpdatedAt(note.getUpdatedAt());
        return dto;
    }
}
