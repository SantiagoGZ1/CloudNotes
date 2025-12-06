package com.cloud.cloudnotes.note_attachment.dto;

import lombok.Data;

@Data
public class NoteAttachmentCreateDto {
    private String fileName;
    private String contentType;
    private String noteId;
}
