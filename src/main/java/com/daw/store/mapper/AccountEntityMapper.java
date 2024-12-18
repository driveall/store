package com.daw.store.mapper;

import com.daw.store.Constants;
import com.daw.store.dynamodb.entity.Account;
import com.daw.store.entity.AccountEntity;
import com.daw.store.entity.ItemEntity;
import com.daw.store.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountEntityMapper {
    public AccountEntity toAccountEntity(Account account, AccountService accountService) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setLogin(account.getLogin());
        accountEntity.setPassword(account.getPassword());
        accountEntity.setEmail(account.getEmail());
        accountEntity.setPhone(account.getPhone());

        accountEntity.setMoney(account.getMoney());
        if (account.getHead() != null && !account.getHead().isEmpty())
            accountEntity.setHead(ItemEntity.fromJson(account.getHead()));
        if (account.getBody() != null && !account.getBody().isEmpty())
            accountEntity.setBody(ItemEntity.fromJson(account.getBody()));
        if (account.getLegs() != null && !account.getLegs().isEmpty())
            accountEntity.setLegs(ItemEntity.fromJson(account.getLegs()));
        if (account.getWeapon() != null && !account.getWeapon().isEmpty())
            accountEntity.setWeapon(ItemEntity.fromJson(account.getWeapon()));
        try {
            Set<String> itemIds = new ObjectMapper().readValue(account.getStorage(), HashSet.class);
            Set<ItemEntity> items = itemIds.stream()
                    .map(itemId -> accountService.getItem(itemId))
                    .collect(Collectors.toSet());
            accountEntity.setStorage(items);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        accountEntity.setCreatedAt(account.getCreatedAt());
        accountEntity.setUpdatedAt(account.getUpdatedAt());
        accountEntity.setPasswordChangedAt(account.getPasswordChangedAt());
        return accountEntity;
    }
    public Account toAccount(AccountEntity accountEntity) {
        Account account = new Account();
        account.setId(Constants.ACCOUNT_PREFIX + accountEntity.getLogin());
        account.setLogin(accountEntity.getLogin());
        account.setPassword(accountEntity.getPassword());
        account.setEmail(accountEntity.getEmail());
        account.setPhone(accountEntity.getPhone());

        account.setMoney(accountEntity.getMoney());
        if (accountEntity.getHead() != null) account.setHead(accountEntity.getHead().toJson());
        if (accountEntity.getBody() != null) account.setBody(accountEntity.getBody().toJson());
        if (accountEntity.getLegs() != null) account.setLegs(accountEntity.getLegs().toJson());
        if (accountEntity.getWeapon() != null) account.setWeapon(accountEntity.getWeapon().toJson());
        try {
            account.setStorage(new ObjectMapper().writeValueAsString(accountEntity.getStorage()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        account.setCreatedAt(accountEntity.getCreatedAt());
        account.setUpdatedAt(accountEntity.getUpdatedAt());
        account.setPasswordChangedAt(accountEntity.getPasswordChangedAt());

        return account;
    }
    public void prepareUpdateEntity(AccountEntity load, AccountEntity accountEntity) {
        if (accountEntity.getPassword() != null) load.setPassword(accountEntity.getPassword());
        if (accountEntity.getEmail() != null) load.setEmail(accountEntity.getEmail());
        if (accountEntity.getPhone() != null) load.setPhone(accountEntity.getPhone());

        if (accountEntity.getMoney() != null) load.setMoney(accountEntity.getMoney());
        if (accountEntity.getHead() != null) load.setHead(accountEntity.getHead());
        if (accountEntity.getBody() != null) load.setBody(accountEntity.getBody());
        if (accountEntity.getLegs() != null) load.setLegs(accountEntity.getLegs());
        if (accountEntity.getWeapon() != null) load.setWeapon(accountEntity.getWeapon());
        if (accountEntity.getStorage() != null) load.setStorage(accountEntity.getStorage());

        if (accountEntity.getCreatedAt() != null) load.setCreatedAt(accountEntity.getCreatedAt());
        if (accountEntity.getUpdatedAt() != null) load.setUpdatedAt(accountEntity.getUpdatedAt());
        if (accountEntity.getPasswordChangedAt() != null) load.setPasswordChangedAt(accountEntity.getPasswordChangedAt());
    }
}
