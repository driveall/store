package com.daw.store.dynamodb;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDbConfig {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.dynamodb.accessKey}")
    private String dynamodbAccessKey;

    @Value("${aws.dynamodb.secretKey}")
    private String dynamodbSecretKey;

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(buildAmazonDynamoDB());
    }

    @Bean
    public AmazonDynamoDB buildAmazonDynamoDB() {
        var amazonDynamoDB = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(awsRegion)
                .withCredentials(
                        new AWSStaticCredentialsProvider(
                                new BasicAWSCredentials(
                                        dynamodbAccessKey,
                                        dynamodbSecretKey
                                )
                        )
                )
                .build();

//        CreateTableRequest request = new CreateTableRequest()
//                .withAttributeDefinitions(new AttributeDefinition(
//                        "id", ScalarAttributeType.S))
//                .withKeySchema(new KeySchemaElement("id", KeyType.HASH))
//                .withProvisionedThroughput(new ProvisionedThroughput(
//                        10L, 10L))
//                .withTableName("account");
//
//        try {
//            CreateTableResult result = amazonDynamoDB.createTable(request);
//            System.out.println(result.getTableDescription().getTableName());
//        } catch (AmazonServiceException e) {
//            System.err.println(e.getErrorMessage());
//            System.exit(1);
//        }
        return amazonDynamoDB;
    }
}