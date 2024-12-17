package com.daw.store.repo;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.*;
import com.daw.store.dynamodb.entity.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class DynamoDBAccountRepository implements AccountRepository {
    final private DynamoDBMapper dynamoDBMapper;
    final private AmazonDynamoDB amazonDynamoDB;

    public void save(String login, String password) {
        dynamoDBMapper.generateCreateTableRequest(Account.class);
        dynamoDBMapper.save(new Account("ACCOUNT:"+login, login, password));
    }

    public boolean exists(String login) {
        dynamoDBMapper.generateCreateTableRequest(Account.class);
        try {
            Account load = dynamoDBMapper.load(Account.class, "ACCOUNT:" + login);
            return load != null;
        } catch (Exception e) {
            return false;
        }
    }
    public String getPassword(String login) {
        Account load = dynamoDBMapper.load(Account.class, "ACCOUNT:"+login);
        return load.getPassword();
    }
    public void delete(String login) {
        Account load = dynamoDBMapper.load(Account.class, "ACCOUNT:"+login);
        dynamoDBMapper.delete(load);
    }

    public void update(String login, String password, String newPassword) {
        Account load = dynamoDBMapper.load(Account.class, "ACCOUNT:"+login);
        // map these two entity
        load.setPassword(newPassword);
        dynamoDBMapper.save(load);
    }
}
