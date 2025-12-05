package com.cloud.cloudnotes.notes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateNoteDto {

    @NotBlank(message = "userId es obligatorio")
    private String userId;

    @NotBlank(message = "title es obligatorio")
    @Size(max = 255, message = "title no puede exceder 255 caracteres")
    private String title;

    @NotBlank(message = "content es obligatorio")
    @Size(max = 10000, message = "content excede el tamaño máximo permitido")
    private String content;
}
