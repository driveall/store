package com.daw.store.repo;

import com.daw.store.dynamodb.entity.Account;
//import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//@Repository
@Deprecated
public class LocalAccountRepository implements AccountRepository {

    private final Map<String, String> accounts = new ConcurrentHashMap<>();

    @Override
    public void save(Account account) {
        if (!exists(account)) {
            accounts.put(account.getLogin(), account.getPassword());
        }
    }

    @Override
    public boolean exists(Account account) {
        return accounts.get(account.getLogin()) != null;
    }

    @Override
    public void delete(Account account) {
        accounts.remove(account.getLogin());
    }

    @Override
    public Account getByLogin(Account acc) {
        var account = new Account();
        account.setLogin(acc.getLogin());
        account.setPassword(accounts.get(acc.getLogin()));
        return account;
    }
}
