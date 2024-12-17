package com.daw.store.repo;

import com.daw.store.dynamodb.entity.Account;

public interface AccountRepository {
    void save(Account account);
    boolean exists(Account account);
    String getPassword(Account account);
    void delete(Account account);
    Account getByLogin(Account account);
}
