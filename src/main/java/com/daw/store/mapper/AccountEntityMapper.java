package com.daw.store.mapper;

import com.daw.store.Constants;
import com.daw.store.dynamodb.entity.Account;
import com.daw.store.entity.AccountEntity;
import com.daw.store.entity.ItemEntity;
import com.daw.store.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountEntityMapper {
    private final ObjectMapper objectMapper;

    public AccountEntityMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public AccountEntity toAccountEntity(Account account, AccountService accountService) {
        var accountEntity = AccountEntity.builder()
                .login(account.getLogin())
                .password(account.getPassword())
                .email(account.getEmail())
                .phone(account.getPhone())
                .money(account.getMoney())
                .level(account.getLevel())
                .points(account.getPoints())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .passwordChangedAt(account.getPasswordChangedAt())
                .build();
        if (account.getHead() != null && !account.getHead().isEmpty())
            accountEntity.setHead(ItemEntity.fromJson(account.getHead(), objectMapper));
        if (account.getBody() != null && !account.getBody().isEmpty())
            accountEntity.setBody(ItemEntity.fromJson(account.getBody(), objectMapper));
        if (account.getLegs() != null && !account.getLegs().isEmpty())
            accountEntity.setLegs(ItemEntity.fromJson(account.getLegs(), objectMapper));
        if (account.getWeapon() != null && !account.getWeapon().isEmpty())
            accountEntity.setWeapon(ItemEntity.fromJson(account.getWeapon(), objectMapper));
        try {
            List<String> itemIds = objectMapper.readValue(account.getStorage(), LinkedList.class);
            Set<ItemEntity> items = itemIds.stream()
                    .map(accountService::getItem)
                    .collect(Collectors.toSet());
            accountEntity.setStorage(items);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return accountEntity;
    }
    public Account toAccount(AccountEntity accountEntity) {
        var account = Account.builder()
                .id(Constants.ACCOUNT_PREFIX + accountEntity.getLogin())
                .login(accountEntity.getLogin())
                .password(accountEntity.getPassword())
                .email(accountEntity.getEmail())
                .phone(accountEntity.getPhone())
                .money(accountEntity.getMoney())
                .level(accountEntity.getLevel())
                .points(accountEntity.getPoints())
                .createdAt(accountEntity.getCreatedAt())
                .updatedAt(accountEntity.getUpdatedAt())
                .passwordChangedAt(accountEntity.getPasswordChangedAt())
                .build();
        if (accountEntity.getHead() != null) account.setHead(accountEntity.getHead().toJson(objectMapper));
        if (accountEntity.getBody() != null) account.setBody(accountEntity.getBody().toJson(objectMapper));
        if (accountEntity.getLegs() != null) account.setLegs(accountEntity.getLegs().toJson(objectMapper));
        if (accountEntity.getWeapon() != null) account.setWeapon(accountEntity.getWeapon().toJson(objectMapper));
        try {
            var itemIds = accountEntity.getStorage().stream()
                    .map(ItemEntity::getId)
                    .collect(Collectors.toList());
            account.setStorage(objectMapper.writeValueAsString(itemIds));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
        if (accountEntity.getLevel() != null) load.setLevel(accountEntity.getLevel());
        if (accountEntity.getPoints() != null) load.setPoints(accountEntity.getPoints());

        if (accountEntity.getCreatedAt() != null) load.setCreatedAt(accountEntity.getCreatedAt());
        if (accountEntity.getUpdatedAt() != null) load.setUpdatedAt(accountEntity.getUpdatedAt());
        if (accountEntity.getPasswordChangedAt() != null) load.setPasswordChangedAt(accountEntity.getPasswordChangedAt());
    }
}
