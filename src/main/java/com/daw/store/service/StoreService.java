package com.daw.store.service;

import com.daw.store.dynamodb.entity.Account;
import com.daw.store.entity.AccountEntity;
import com.daw.store.entity.ItemEntity;
import com.daw.store.enums.ItemType;
import com.daw.store.mapper.AccountEntityMapper;
import com.daw.store.mapper.ItemEntityMapper;
import com.daw.store.repo.AccountRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

@Service
public class StoreService {

    @Value("${password.hash.salt}")
    private String salt;

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";

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
        if (!accountExists(accountEntity.getLogin())) {
            accountEntity.setPassword(hashPassword(accountEntity.getPassword()).get());
            accountEntity.setMoney(20);
            accountEntity.setLevel(1);
            accountEntity.setPoints(0);
            accountEntity.setStorage(new HashSet<>());
            accountEntity.setCreatedAt(DateTime.now().toString());
            accountEntity.setUpdatedAt(DateTime.now().toString());
            accountEntity.setPasswordChangedAt(DateTime.now().toString());

            accountRepository.save(accountEntityMapper.toAccount(accountEntity));
        }
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

    public void buy(String login, String itemId) {
        var account = getByLogin(login);
        var item = getItem(itemId);
        if (account.getMoney() >= item.getPrice()) {
            account.getStorage().add(getItem(itemId));
            var accountToUpdate = AccountEntity.builder()
                    .login(login)
                    .money(account.getMoney() - item.getPrice())
                    .storage(account.getStorage())
                    .build();
            updateAccount(accountToUpdate);
        }
    }

    public void sell(String login, String itemId) {
        var account = getByLogin(login);
        var item = getItem(itemId);
        account.getStorage().remove(getItem(itemId));
        unwear(login, itemId);

        var accountToUpdate = AccountEntity.builder()
                .login(login)
                .money(account.getMoney() + item.getPrice())
                .storage(account.getStorage())
                .build();
        updateAccount(accountToUpdate);
    }

    public void wear(String login, String itemId) {
        var item = getItem(itemId);

        var accountToUpdate = new AccountEntity();
        accountToUpdate.setLogin(login);
        switch (item.getType()) {
            case ItemType.BODY -> accountToUpdate.setBody(item);
            case ItemType.HEAD -> accountToUpdate.setHead(item);
            case ItemType.LEGS -> accountToUpdate.setLegs(item);
            case ItemType.WEAPON -> accountToUpdate.setWeapon(item);
        }
        updateAccount(accountToUpdate);
    }

    public void unwear(String login, String itemId) {
        var item = getItem(itemId);

        var accountToUpdate = new AccountEntity();
        accountToUpdate.setLogin(login);
        switch (item.getType()) {
            case ItemType.BODY -> accountToUpdate.setBody(new ItemEntity());
            case ItemType.HEAD -> accountToUpdate.setHead(new ItemEntity());
            case ItemType.LEGS -> accountToUpdate.setLegs(new ItemEntity());
            case ItemType.WEAPON -> accountToUpdate.setWeapon(new ItemEntity());
        }
        updateAccount(accountToUpdate);
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

    public List<ItemEntity> getAllItems() {
        var items = accountRepository.getAllItems();
        List<ItemEntity> itemEntityList = new LinkedList<>();
        for (Account e : items) {
            itemEntityList.add(itemEntityMapper.toItemEntity(e));
        }
        return itemEntityList;
    }

    public List<AccountEntity> getAllAccounts() {
        var items = accountRepository.getAllAccounts();
        List<AccountEntity> accountEntityList = new LinkedList<>();
        for (Account e : items) {
            accountEntityList.add(accountEntityMapper.toAccountEntity(e, this));
        }
        return accountEntityList;
    }

    public boolean login(AccountEntity accountEntity, String password) {
        return hashPassword(password).get().equals(accountEntity.getPassword());
    }

    public Optional<String> hashPassword(String password) {
        char[] chars = password.toCharArray();
        byte[] bytes = salt.getBytes();

        var spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);

        Arrays.fill(chars, Character.MIN_VALUE);

        try {
            var fac = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] securePassword = fac.generateSecret(spec).getEncoded();
            return Optional.of(Base64.getEncoder().encodeToString(securePassword));

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            System.err.println("Exception encountered in hashPassword()");
            return Optional.empty();
        } finally {
            spec.clearPassword();
        }
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
