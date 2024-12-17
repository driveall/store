package com.daw.store.repo;

public interface AccountRepository {
    void save(String login, String pass);
    boolean exists(String login);
    String getPassword(String login);
    void delete(String login);
}
