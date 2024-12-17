package com.daw.store.service;

import com.daw.store.dynamodb.entity.Account;
import com.daw.store.entity.AccountEntity;
import com.daw.store.mapper.AccountEntityMapper;
import com.daw.store.repo.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public boolean accountExists(String login) {
        var account = new Account();
        account.setLogin(login);
        return accountRepository.exists(account);
    }

    public void createAccount(AccountEntity accountEntity) {
        var account = new Account();
        account.setLogin(accountEntity.getLogin());
        account.setPassword(accountEntity.getPassword());
        accountRepository.save(account);
    }

    public boolean login(AccountEntity accountEntity) {
        var account = new Account();
        account.setLogin(accountEntity.getLogin());
        var password = accountRepository.getByLogin(account).getPassword();
        return accountEntity.getPassword().equals(password);
    }

    public void deleteAccount(AccountEntity accountEntity) {
        var account = new Account();
        account.setLogin(accountEntity.getLogin());
        accountRepository.delete(account);
    }

    public boolean passwordVerification(String pass1, String pass2) {

        return pass1 != null && pass1.equals(pass2);
    }

    public AccountEntity getByLogin(String login) {
        var account = new Account();
        account.setLogin(login);
        account = accountRepository.getByLogin(account);

        if (account != null) {
            return AccountEntityMapper.toAccountEntity(account);
        } else return null;
    }
}
