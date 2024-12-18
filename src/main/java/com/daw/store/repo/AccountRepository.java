package com.daw.store.repo;

import com.daw.store.dynamodb.entity.Account;
import com.daw.store.dynamodb.entity.Item;

public interface AccountRepository {
    void save(Account account);
    boolean exists(Account account);
    void delete(Account account);
    Account getByLogin(Account account);
    Item getByItemId(String itemId);
}
