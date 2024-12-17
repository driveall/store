package com.daw.store.mapper;

import com.daw.store.Constants;
import com.daw.store.dynamodb.entity.Account;
import com.daw.store.entity.AccountEntity;
import org.springframework.stereotype.Service;

@Service
public class AccountEntityMapper {
    public AccountEntity toAccountEntity(Account account) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setLogin(account.getLogin());
        accountEntity.setPassword(account.getPassword());
        accountEntity.setEmail(account.getEmail());
        accountEntity.setPhone(account.getPhone());
        return accountEntity;
    }
    public Account toAccount(AccountEntity accountEntity, String hashedPassword) {
        Account account = new Account();
        account.setId(Constants.ACCOUNT_PREFIX + accountEntity.getLogin());
        account.setLogin(accountEntity.getLogin());
        account.setPassword(hashedPassword);
        account.setEmail(accountEntity.getEmail());
        account.setPhone(accountEntity.getPhone());
        return account;
    }
}
