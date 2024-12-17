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
}
