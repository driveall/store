package com.daw.store.dynamodb.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.*;

import static com.daw.store.Constants.ATTRIBUTE_ID;
import static com.daw.store.Constants.TABLE_NAME;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = TABLE_NAME)
public class Account {
    @DynamoDBHashKey(attributeName = ATTRIBUTE_ID)
    @DynamoDBAttribute
    private String id;
    @DynamoDBAttribute
    private String login;
    @DynamoDBAttribute
    private String password;
    @DynamoDBAttribute
    private String email;
    @DynamoDBAttribute
    private String phone;

    @DynamoDBAttribute
    private Integer money;
    @DynamoDBAttribute
    private String head;
    @DynamoDBAttribute
    private String body;
    @DynamoDBAttribute
    private String legs;
    @DynamoDBAttribute
    private String weapon;
    @DynamoDBAttribute
    private String storage;
    @DynamoDBAttribute
    private Integer level;
    @DynamoDBAttribute
    private Integer points;

    @DynamoDBAttribute
    private String createdAt;
    @DynamoDBAttribute
    private String updatedAt;
    @DynamoDBAttribute
    private String passwordChangedAt;

    //ITEM
    @DynamoDBAttribute
    private String name;
    @DynamoDBAttribute
    private String description;
    @DynamoDBAttribute
    private String image;
    @DynamoDBAttribute
    private Integer price;
    @DynamoDBAttribute
    private String type;

}
