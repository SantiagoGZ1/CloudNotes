package com.cloud.cloudnotes.notes;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NoteService {

    public final NoteRepository noteRepository;

    public Note createNote(Note note, String userId) {
        note.setNoteId(UUID.randomUUID().toString());
        note.setUserId(userId);
        note.setCreatedAt(Instant.now());
        note.setUpdatedAt(Instant.now());
        return noteRepository.save(note);
    }
}
