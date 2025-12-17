package com.cloud.cloudnotes.user;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@Data
public class UserEntity {
    private String userId;
    private String username;
    private String password;
    private String role;

    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }
}
