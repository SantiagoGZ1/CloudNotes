package com.cloud.cloudnotes.note_attachment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class NoteAttachmentService {

    private final S3Client s3Client;
    private final NoteAttachmentRepository repository;

    public NoteAttachmentService(S3Client s3Client, NoteAttachmentRepository repository) {
        this.s3Client = s3Client;
        this.repository = repository;
    }

    @Value("${app.bucket.name}")
    private String bucketName;

    public NoteAttachment uploadAttachment(String noteId, MultipartFile file) throws IOException {

        // 1. Crear ID
        String attachmentId = UUID.randomUUID().toString();

        // 2. Subir el archivo a S3
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key("notes/" + noteId + "/" + attachmentId + "-" + file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );

        // 3. Crear URL pública (simulada con LocalStack)
        String fileUrl = "http://localhost:4566/" + bucketName + "/notes/"
                + noteId + "/" + attachmentId + "-" + file.getOriginalFilename();

        // 4. Crear objeto metadata
        NoteAttachment attachment = new NoteAttachment();
        attachment.setNoteAttachmentId(attachmentId);
        attachment.setNoteId(noteId);
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFileUrl(fileUrl);
        attachment.setContentType(file.getContentType());

        // 5. Guardar metadata en DynamoDB
        repository.save(attachment);

        return attachment;
    }
}
