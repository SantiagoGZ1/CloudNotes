package com.cloud.cloudnotes.note_attachment.dto;

import lombok.Data;

@Data
public class NoteAttachmentResponseDto {
    private String noteAttachmentId;
    private String noteId;
    private String fileName;
    private String fileUrl;
    private String contentType;
}
