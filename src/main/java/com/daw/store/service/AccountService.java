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
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;

@Service
public class AccountService {

    @Value("${password.hash.salt}")
    private String salt;

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 512;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA512";

    private final AccountRepository accountRepository;
    private final AccountEntityMapper accountEntityMapper;
    private final ItemEntityMapper itemEntityMapper;

    public AccountService(AccountRepository accountRepository, AccountEntityMapper accountEntityMapper, ItemEntityMapper itemEntityMapper) {
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
        accountEntity.setPassword(hashPassword(accountEntity.getPassword()).get());
        accountEntity.setMoney(20);
        var storage = new HashSet<ItemEntity>();
        storage.add(getItem("ITEM:4"));
        storage.add(getItem("ITEM:1"));

        accountEntity.setStorage(storage);
        accountEntity.setCreatedAt(DateTime.now().toString());
        accountEntity.setUpdatedAt(DateTime.now().toString());
        accountEntity.setPasswordChangedAt(DateTime.now().toString());
        accountRepository.save(accountEntityMapper.toAccount(accountEntity));
    }

    public void updateAccount(AccountEntity accountEntity) {
        var load = getByLogin(accountEntity.getLogin());
        if (accountEntity.getPassword() != null) {
            accountEntity.setPassword(hashPassword(accountEntity.getPassword()).get());
        }
        accountEntityMapper.prepareUpdateEntity(load, accountEntity);
        accountRepository.save(accountEntityMapper.toAccount(load));
    }

    public boolean login(AccountEntity accountEntity, String password) {
        return hashPassword(password).get().equals(accountEntity.getPassword());
    }

    public void deleteAccount(AccountEntity accountEntity) {
        if (accountEntity.getPassword() != null) {
            accountEntity.setPassword(hashPassword(accountEntity.getPassword()).get());
        }
        accountRepository.delete(accountEntityMapper.toAccount(accountEntity));
    }

    public boolean passwordVerification(String pass1, String pass2) {
        return pass1 != null && pass1.equals(pass2);
    }

    public AccountEntity getByLogin(String login) {
        var account = new Account();
        account.setLogin(login);
        account = accountRepository.getByLogin(account);

        if (account != null) {
            return accountEntityMapper.toAccountEntity(account, this);
        } else return null;
    }

    public Optional<String> hashPassword (String password) {
        char[] chars = password.toCharArray();
        byte[] bytes = salt.getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, bytes, ITERATIONS, KEY_LENGTH);

        Arrays.fill(chars, Character.MIN_VALUE);

        try {
            SecretKeyFactory fac = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] securePassword = fac.generateSecret(spec).getEncoded();
            return Optional.of(Base64.getEncoder().encodeToString(securePassword));

        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            System.err.println("Exception encountered in hashPassword()");
            return Optional.empty();
        } finally {
            spec.clearPassword();
        }
    }

    public ItemEntity getItem(String itemId) {
        var item = accountRepository.getByItemId(itemId);
        return item != null ? itemEntityMapper.toItemEntity(item) : null;
    }

    public void buy(String login, String itemId) {
        var account = getByLogin(login);
        var item = getItem(itemId);
        if (account.getMoney() >= item.getPrice()) {
            var accountToUpdate = new AccountEntity();
            accountToUpdate.setLogin(login);
            accountToUpdate.setMoney(account.getMoney() - item.getPrice());
            account.getStorage().add(getItem(itemId));
            accountToUpdate.setStorage(account.getStorage());
            updateAccount(accountToUpdate);
        }
    }

    public void sell(String login, String itemId) {
        var account = getByLogin(login);
        var item = getItem(itemId);

        var accountToUpdate = new AccountEntity();
        accountToUpdate.setLogin(login);
        accountToUpdate.setMoney(account.getMoney() + item.getPrice());
        account.getStorage().remove(getItem(itemId));
        accountToUpdate.setStorage(account.getStorage());
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
}
