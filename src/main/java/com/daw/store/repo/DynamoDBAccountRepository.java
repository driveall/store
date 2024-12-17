package com.daw.store.repo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.daw.store.Constants;
import com.daw.store.dynamodb.entity.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.daw.store.Constants.ACCOUNT_PREFIX;

@Repository
@AllArgsConstructor
public class DynamoDBAccountRepository implements AccountRepository {
    final private DynamoDBMapper dynamoDBMapper;

    public void save(String login, String password) {
        dynamoDBMapper.generateCreateTableRequest(Account.class);
        dynamoDBMapper.save(new Account(ACCOUNT_PREFIX+login, login, password));
    }

    public boolean exists(String login) {
        dynamoDBMapper.generateCreateTableRequest(Account.class);
        try {
            Account load = dynamoDBMapper.load(Account.class, ACCOUNT_PREFIX + login);
            return load != null;
        } catch (Exception e) {
            return false;
        }
    }
    public String getPassword(String login) {
        Account load = dynamoDBMapper.load(Account.class, ACCOUNT_PREFIX + login);
        return load.getPassword();
    }
    public void delete(String login) {
        Account load = dynamoDBMapper.load(Account.class, ACCOUNT_PREFIX + login);
        dynamoDBMapper.delete(load);
    }

    public void update(String login, String password, String newPassword) {
        Account load = dynamoDBMapper.load(Account.class, ACCOUNT_PREFIX + login);
        load.setPassword(newPassword);
        dynamoDBMapper.save(load);
    }
}
