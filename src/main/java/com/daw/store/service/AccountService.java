package com.daw.store.service;

import com.daw.store.repo.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public boolean accountExists(String login) {
        return accountRepository.exists(login);
    }

    public void createAccount(String login, String pass) {
        accountRepository.save(login, pass);
    }

    public boolean login(String login, String pass) {
        var p = accountRepository.getPassword(login);
        return login != null && pass != null && pass.equals(p);
    }

    public void deleteAccount(String login) {
        accountRepository.delete(login);
    }

    public boolean passwordConfirmed(String pass1, String pass2) {
        return pass1 != null && pass1.equals(pass2);
    }
}
