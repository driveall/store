package com.daw.store.dynamodb;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.daw.store.Constants.*;

@Configuration
public class DynamoDbConfig {

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

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
                                        accessKey,
                                        secretKey
                                )
                        )
                )
                .build();
        createTable(amazonDynamoDB);
        return amazonDynamoDB;
    }

    private void createTable(AmazonDynamoDB amazonDynamoDB) {
        if (!amazonDynamoDB.listTables().getTableNames().contains(TABLE_NAME)) {
            CreateTableRequest request = new CreateTableRequest()
                    .withAttributeDefinitions(new AttributeDefinition(
                            ATTRIBUTE_ID, ScalarAttributeType.S))
                    .withKeySchema(new KeySchemaElement(
                            ATTRIBUTE_ID, KeyType.HASH))
                    .withProvisionedThroughput(new ProvisionedThroughput(
                            CAPACITY_UNITS, CAPACITY_UNITS))
                    .withTableName(TABLE_NAME);

            try {
                CreateTableResult result = amazonDynamoDB.createTable(request);
                System.out.println(result.getTableDescription().getTableName());
            } catch (AmazonServiceException e) {
                System.err.println(e.getErrorMessage());
                System.exit(1);
            }
        }
    }
}