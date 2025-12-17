package com.cloud.cloudnotes.user;

import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class UserRepository {

    private final DynamoDbTable<UserEntity> userTable;

    public UserRepository(DynamoDbEnhancedClient enhancedClient) {
        this.userTable = enhancedClient.table("users", TableSchema.fromBean(UserEntity.class));
    }

    public void save(UserEntity userEntity) {
        userTable.putItem(userEntity);
    }

    public UserEntity findByUsername(String username) {
        return userTable.scan()
                .items()
                .stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public UserEntity findById(String id) {
        return userTable.getItem(Key.builder().partitionValue(id).build());
    }

}
