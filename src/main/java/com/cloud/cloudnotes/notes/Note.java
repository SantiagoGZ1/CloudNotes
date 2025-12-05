package com.cloud.cloudnotes.notes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;

@DynamoDbBean
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {

    private String noteId;
    private String userId;
    private String title;
    private String content;
    private Instant createdAt;
    private Instant updatedAt;

    @DynamoDbPartitionKey
    public String getNoteId() {
        return noteId;
    }
}

