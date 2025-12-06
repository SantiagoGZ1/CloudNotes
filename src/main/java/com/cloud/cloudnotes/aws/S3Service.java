package com.cloud.cloudnotes.aws;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Service
@Data
public class S3Service {

    private final S3Client s3;

    @Value("${app.bucket.name}")
    private String bucketName;

    public S3Service(S3Client s3) {
        this.s3 = s3;
    }

    public String uploadFile(MultipartFile file) {
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();

        PutObjectRequest put = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        try {
            s3.putObject(put, RequestBody.fromBytes(file.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Error subiendo archivo", e);
        }

        return "https://s3.localhost.localstack.cloud:4566/" + bucketName + "/" + key;
    }
}
