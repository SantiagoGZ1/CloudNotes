package com.cloud.cloudnotes.note_attachment;

import com.cloud.cloudnotes.note_attachment.dto.NoteAttachmentMapper;
import com.cloud.cloudnotes.note_attachment.dto.NoteAttachmentResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/notes")
@AllArgsConstructor
public class NoteAttachmentController {

    private final NoteAttachmentService service;
    private final NoteAttachmentMapper mapper;

    @PostMapping(value = "/{noteId}/attachments", consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NoteAttachmentResponseDto> uploadAttachment(@PathVariable String noteId, @RequestPart("file") MultipartFile file) {
        try {
            NoteAttachment saved = service.uploadAttachment(noteId, file);
            NoteAttachmentResponseDto dto = mapper.toResponseDto(saved);
            java.net.URI location = java.net.URI.create("/api/notes/" + noteId + "/attachments/" + dto.getNoteAttachmentId());
            return org.springframework.http.ResponseEntity.created(location).body(dto);
        } catch (IOException e) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
