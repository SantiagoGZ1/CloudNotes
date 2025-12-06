package com.cloud.cloudnotes.note_attachment;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;

@Repository
public class NoteAttachmentRepository {

    private final DynamoDbTable<NoteAttachment> table;

    public NoteAttachmentRepository(DynamoDbEnhancedClient client) {
        this.table = client.table("NoteAttachments", TableSchema.fromBean(NoteAttachment.class));
    }

    public NoteAttachment save(NoteAttachment noteAttachment) {
        table.putItem(noteAttachment);
        return noteAttachment;
    }

    public NoteAttachment findById(String id) {
        return table.getItem(Key.builder().partitionValue(id).build());
    }

    public List<NoteAttachment> findAll() {
        return table.scan().items().stream().toList();
    }

    public void deleteById(String id) {
        table.deleteItem(Key.builder().partitionValue(id).build());
    }

    public List<NoteAttachment> findByNoteId(String noteId) {

        DynamoDbIndex<NoteAttachment> index = table.index("noteId-index");

        return index.query(r -> r.queryConditional(
                        QueryConditional.keyEqualTo(
                                Key.builder().partitionValue(noteId).build()
                        )
                ))
                .stream()                      // stream de Page<NoteAttachment>
                .flatMap(page -> page.items().stream())  // aquí sí existe items()
                .toList();
    }
}

