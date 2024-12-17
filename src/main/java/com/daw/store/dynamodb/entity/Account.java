package com.daw.store.dynamodb.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.*;

import static com.daw.store.Constants.ATTRIBUTE_ID;
import static com.daw.store.Constants.TABLE_NAME;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
