package com.cloud.cloudnotes.notes.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class NoteResponseDto {
    private String noteId;
    private String userId;
    private String title;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;
}
