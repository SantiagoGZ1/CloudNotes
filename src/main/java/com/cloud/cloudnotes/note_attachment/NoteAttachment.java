package com.cloud.cloudnotes.note_attachment;

import lombok.AllArgsConstructor;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

@Data
@AllArgsConstructor
@DynamoDbBean
public class NoteAttachment {
    private String noteAttachmentId;
    private String noteId;
    private String fileName;
    private String fileUrl;
    private String contentType;

    @DynamoDbPartitionKey
    public String getNoteAttachmentId() {
        return noteAttachmentId;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "noteId-index")
    public String getNoteId() {
        return noteId;
    }
}
