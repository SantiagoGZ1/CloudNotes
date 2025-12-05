package com.cloud.cloudnotes.notes;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NoteService {

    public final NoteRepository noteRepository;

    public Note createNote(Note note) {
        note.setNoteId(UUID.randomUUID().toString());
        note.setCreatedAt(Instant.now());
        note.setUpdatedAt(Instant.now());
        return noteRepository.save(note);
    }

    public Optional<Note> getNoteById(String id) {
        return Optional.ofNullable(noteRepository.findById(id));
    }

    public List<Note> getAllNotesByUser(String userId) {
        return noteRepository.findAll()
                .stream()
                .filter(note -> note.getUserId().equals(userId))
                .toList();
    }

    public Optional<Note> updateNote(String id, Note updatedNote, String userId) {
        Note existing = noteRepository.findById(id);

        if (existing == null || !existing.getUserId().equals(userId)) {
            return Optional.empty(); // No encontrada o no pertenece al usuario
        }

        existing.setTitle(updatedNote.getTitle());
        existing.setContent(updatedNote.getContent());
        existing.setUpdatedAt(Instant.now());

        return Optional.of(noteRepository.save(existing));
    }

    public boolean deleteNote(String id, String userId) {
        Note existing = noteRepository.findById(id);

        if (existing == null || !existing.getUserId().equals(userId)) {
            return false;
        }

        noteRepository.deleteById(id);
        return true;
    }
}
