package com.daw.store.dynamodb;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import com.daw.store.dynamodb.entity.Account;
import com.daw.store.enums.ItemType;
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
        createTableAndFillWithItems(amazonDynamoDB);
        return amazonDynamoDB;
    }

    private void createTableAndFillWithItems(AmazonDynamoDB amazonDynamoDB) {
        if (!amazonDynamoDB.listTables().getTableNames().contains(TABLE_NAME)) {
            createTable(amazonDynamoDB);
            fillWithItems(amazonDynamoDB);
        }
    }

    private void deleteTable(AmazonDynamoDB amazonDynamoDB) {
        amazonDynamoDB.deleteTable(TABLE_NAME);
    }

    private void createTable(AmazonDynamoDB amazonDynamoDB) {
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

    private void fillWithItems(AmazonDynamoDB amazonDynamoDB) {
        var mapper = new DynamoDBMapper(amazonDynamoDB);

        var item = Account.builder()
                .id(ITEM_PREFIX + "1")
                .name("Winter Hat")
                .description("Low armored item")
                .price(5)
                .points(1)
                .level(1)
                .type(ItemType.HEAD.name())
                .image("1")
                .build();
        mapper.save(item);

        item = Account.builder()
                .id(ITEM_PREFIX + "2")
                .name("Winter Jacket")
                .description("Low armored item")
                .price(5)
                .points(1)
                .level(1)
                .type(ItemType.BODY.name())
                .image("2")
                .build();
        mapper.save(item);

        item = Account.builder()
                .id(ITEM_PREFIX + "3")
                .name("Winter Boots")
                .description("Low armored item")
                .price(5)
                .points(1)
                .level(1)
                .type(ItemType.LEGS.name())
                .image("3")
                .build();
        mapper.save(item);

        item = Account.builder()
                .id(ITEM_PREFIX + "4")
                .name("TT Pistol")
                .description("Low damage item")
                .price(5)
                .points(4)
                .level(1)
                .type(ItemType.WEAPON.name())
                .image("4")
                .build();
        mapper.save(item);

        item = Account.builder()
                .id(ITEM_PREFIX + "5")
                .name("Army Hat")
                .description("Low armored item")
                .price(20)
                .points(2)
                .level(2)
                .type(ItemType.HEAD.name())
                .image("5")
                .build();
        mapper.save(item);

        item = Account.builder()
                .id(ITEM_PREFIX + "6")
                .name("Army Jacket")
                .description("Low armored item")
                .price(20)
                .points(2)
                .level(2)
                .type(ItemType.BODY.name())
                .image("6")
                .build();
        mapper.save(item);

        item = Account.builder()
                .id(ITEM_PREFIX + "7")
                .name("Army Boots")
                .description("Low armored item")
                .price(20)
                .points(2)
                .level(2)
                .type(ItemType.LEGS.name())
                .image("7")
                .build();
        mapper.save(item);

        item = Account.builder()
                .id(ITEM_PREFIX + "8")
                .name("Desert Eagle")
                .description("Low damage item")
                .price(35)
                .points(9)
                .level(2)
                .type(ItemType.WEAPON.name())
                .image("8")
                .build();
        mapper.save(item);
    }

}