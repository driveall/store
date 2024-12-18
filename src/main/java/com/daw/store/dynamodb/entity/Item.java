package com.daw.store.dynamodb.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.daw.store.enums.ItemType;
import lombok.*;

import static com.daw.store.Constants.ATTRIBUTE_ID;
import static com.daw.store.Constants.TABLE_NAME;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = TABLE_NAME)
public class Item {
    @DynamoDBHashKey(attributeName = ATTRIBUTE_ID)
    @DynamoDBAttribute
    private String id;
    @DynamoDBAttribute
    private String name;
    @DynamoDBAttribute
    private Integer points;
    @DynamoDBAttribute
    private String description;
    @DynamoDBAttribute
    private String image;
    @DynamoDBAttribute
    private Integer level;
    @DynamoDBAttribute
    private Integer price;
    @DynamoDBAttribute
    private ItemType type;
}
