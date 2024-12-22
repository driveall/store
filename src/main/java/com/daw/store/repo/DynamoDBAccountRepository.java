package com.daw.store.repo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.daw.store.dynamodb.entity.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.daw.store.Constants.*;

@Repository
@AllArgsConstructor
public class DynamoDBAccountRepository implements AccountRepository {
    private final DynamoDBMapper dynamoDBMapper;
    private final AmazonDynamoDB dynamoDB;

    @Override
    public void save(Account account) {
        dynamoDBMapper.save(account);
    }

    @Override
    public boolean exists(Account account) {
        try {
            var load = dynamoDBMapper.load(Account.class, ACCOUNT_PREFIX + account.getLogin());
            return load != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void delete(Account account) {
        var load = dynamoDBMapper.load(Account.class, ACCOUNT_PREFIX + account.getLogin());
        dynamoDBMapper.delete(load);
    }

    @Override
    public Account getByLogin(Account account) {
        return dynamoDBMapper.load(Account.class, ACCOUNT_PREFIX + account.getLogin());
    }

    @Override
    public Account getByItemId(Account account) {
        return dynamoDBMapper.load(Account.class, account.getId());
    }

    @Override
    public List<Account> getAllItems() {
        var res = dynamoDBMapper.scan(Account.class, new DynamoDBScanExpression()
                .withFilterExpression("begins_with(id, :id)")
                .withExpressionAttributeValues(Map.of(":id", new AttributeValue(ITEM_PREFIX))));
        return res.parallelStream().toList();
    }

    @Override
    public List<Account> getAllAccounts() {
        var res = dynamoDBMapper.scan(Account.class, new DynamoDBScanExpression()
                .withFilterExpression("begins_with(id, :id)")
                .withExpressionAttributeValues(Map.of(":id", new AttributeValue(ACCOUNT_PREFIX))));
        return res.parallelStream().toList();
    }

}
