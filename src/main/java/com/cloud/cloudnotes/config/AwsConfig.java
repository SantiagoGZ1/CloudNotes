package com.cloud.cloudnotes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class AwsConfig {
    @Value("${aws.accessKeyId}")
    private String awsAccessKeyId;

    @Value("${aws.secretKey}")
    private String awsSecretKey;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${cloud.aws.localstack-enabled:false}")
    private boolean localstackEnabled;

    @Value("${cloud.aws.localstack-url:http://localhost:4566}")
    private String localstackUrl;


    // ----------- S3 CLIENT -----------
    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create("https://s3.localhost.localstack.cloud:4566"))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create("test", "test")
                        )
                )
                .region(Region.US_EAST_1)
                .build();
    }

    // ----------- DYNAMODB CLIENT -----------

    @Bean
    public DynamoDbClient dynamoDbClient() {
        final String ACCESS_KEY = "test";
        final String SECRET_KEY = "test";

        AwsCredentialsProvider credentials =
                StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(ACCESS_KEY, SECRET_KEY));

        Region region = Region.US_EAST_1;

        return DynamoDbClient.builder()
                .region(region)
                .credentialsProvider(credentials)
                .endpointOverride(URI.create("https://localhost.localstack.cloud:4566"))
                .build();
    }
}
