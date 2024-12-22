package com.daw.store.repo;

import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.daw.store.dynamodb.entity.Account;

import java.util.List;

public interface AccountRepository {
    void save(Account account);
    boolean exists(Account account);
    void delete(Account account);
    Account getByLogin(Account account);
    Account getByItemId(Account account);
    List<Account> getAllItems();
    List<Account> getAllAccounts();
}
