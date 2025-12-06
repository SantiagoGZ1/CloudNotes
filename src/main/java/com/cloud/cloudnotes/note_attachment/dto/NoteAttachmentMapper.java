package com.cloud.cloudnotes.note_attachment.dto;

import com.cloud.cloudnotes.note_attachment.NoteAttachment;
import org.springframework.stereotype.Component;

@Component
public class NoteAttachmentMapper {
    public NoteAttachmentResponseDto toResponseDto(NoteAttachment noteAttachment) {
        NoteAttachmentResponseDto dto = new NoteAttachmentResponseDto();
        dto.setNoteAttachmentId(noteAttachment.getNoteAttachmentId());
        dto.setNoteId(noteAttachment.getNoteId());
        dto.setFileName(noteAttachment.getFileName());
        dto.setFileUrl(noteAttachment.getFileUrl());
        dto.setContentType(noteAttachment.getContentType());
        return dto;
    }

    public NoteAttachment toEntity(NoteAttachmentCreateDto createDto, String fileUrl, String noteAttachmentId) {
        return new NoteAttachment(
                noteAttachmentId,
                createDto.getNoteId(),
                createDto.getFileName(),
                fileUrl,
                createDto.getContentType()
        );
    }
}
