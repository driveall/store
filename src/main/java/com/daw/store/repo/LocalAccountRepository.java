package com.daw.store.repo;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//@Repository
public class LocalAccountRepository implements AccountRepository {

    private Map<String, String> accounts = new ConcurrentHashMap<>();

    @Override
    public void save(String login, String password) {
        if (!exists(login)) {
            accounts.put(login, password);
        }
    }

    @Override
    public boolean exists(String login) {
        return accounts.get(login) != null;
    }

    @Override
    public String getPassword(String login) {
        if (exists(login)) {
            return accounts.get(login);
        }
        return null;
    }

    @Override
    public void delete(String login) {
        accounts.remove(login);
    }
}
