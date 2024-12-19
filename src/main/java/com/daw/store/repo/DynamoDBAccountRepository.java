package com.daw.store.repo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.daw.store.dynamodb.entity.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.daw.store.Constants.ACCOUNT_PREFIX;

@Repository
@AllArgsConstructor
public class DynamoDBAccountRepository implements AccountRepository {
    private final DynamoDBMapper dynamoDBMapper;

    @Override
    public void save(Account account) {
        dynamoDBMapper.save(account);
    }

    @Override
    public boolean exists(Account account) {
        try {
            Account load = dynamoDBMapper.load(Account.class, ACCOUNT_PREFIX + account.getLogin());
            return load != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void delete(Account account) {
        Account load = dynamoDBMapper.load(Account.class, ACCOUNT_PREFIX + account.getLogin());
        dynamoDBMapper.delete(load);
    }

    @Override
    public Account getByLogin(Account account) {
        return dynamoDBMapper.load(Account.class, ACCOUNT_PREFIX + account.getLogin());
    }

    @Override
    public Account getByItemId(String itemId) {
        return dynamoDBMapper.load(Account.class, itemId);
    }

    public void update(Account account) {
        Account load = dynamoDBMapper.load(Account.class, ACCOUNT_PREFIX + account.getLogin());
        load.setPassword(account.getPassword());
        dynamoDBMapper.save(load);
    }
}
