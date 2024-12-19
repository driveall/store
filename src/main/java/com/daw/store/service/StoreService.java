package com.daw.store.service;

import com.daw.store.dynamodb.entity.Account;
import com.daw.store.entity.AccountEntity;
import com.daw.store.entity.ItemEntity;
import com.daw.store.mapper.AccountEntityMapper;
import com.daw.store.mapper.ItemEntityMapper;
import com.daw.store.repo.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class StoreService {

    private final AccountRepository accountRepository;
    private final AccountEntityMapper accountEntityMapper;
    private final ItemEntityMapper itemEntityMapper;

    public StoreService(AccountRepository accountRepository, AccountEntityMapper accountEntityMapper, ItemEntityMapper itemEntityMapper) {
        this.accountRepository = accountRepository;
        this.accountEntityMapper = accountEntityMapper;
        this.itemEntityMapper = itemEntityMapper;
    }

    public boolean accountExists(String login) {
        var account = new Account();
        account.setLogin(login);
        return accountRepository.exists(account);
    }

    public void createAccount(AccountEntity accountEntity) {
        accountRepository.save(accountEntityMapper.toAccount(accountEntity));
    }

    public void updateAccount(AccountEntity accountEntity) {
        var load = getByLogin(accountEntity.getLogin());
        prepareUpdateEntity(load, accountEntity);
        accountRepository.save(accountEntityMapper.toAccount(load));
    }

    public void deleteAccount(String login) {
        var accountEntity = AccountEntity.builder()
                .login(login)
                .build();
        accountRepository.delete(accountEntityMapper.toAccount(accountEntity));
    }

    public AccountEntity getByLogin(String login) {
        var account = new Account();
        account.setLogin(login);
        account = accountRepository.getByLogin(account);

        if (account != null) {
            return accountEntityMapper.toAccountEntity(account, this);
        } else return null;
    }

    public ItemEntity getItem(String itemId) {
        var item = accountRepository.getByItemId(Account.builder()
                .id(itemId)
                .build());
        return item != null ? itemEntityMapper.toItemEntity(item) : null;
    }

    private void prepareUpdateEntity(AccountEntity load, AccountEntity accountEntity) {
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
