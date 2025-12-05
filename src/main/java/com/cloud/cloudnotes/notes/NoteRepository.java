package com.cloud.cloudnotes.notes;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;

@Repository
public class NoteRepository {

    private final DynamoDbTable<Note> table;

    public NoteRepository(DynamoDbEnhancedClient client) {
        this.table = client.table("Notess", TableSchema.fromBean(Note.class));
    }

    public Note save(Note Note) {
        table.putItem(Note);
        return Note;
    }

    public Note findById(String id) {
        return table.getItem(Key.builder().partitionValue(id).build());
    }

    public List<Note> findAll() {
        return table.scan().items().stream().toList();
    }

    public void deleteById(String id) {
        table.deleteItem(Key.builder().partitionValue(id).build());
    }
}
