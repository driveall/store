package com.daw.store.service;

import com.daw.store.dynamodb.entity.Account;
import com.daw.store.entity.AccountEntity;
import com.daw.store.mapper.AccountEntityMapper;
import com.daw.store.repo.AccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
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

    public AccountService(AccountRepository accountRepository, AccountEntityMapper accountEntityMapper) {
        this.accountRepository = accountRepository;
        this.accountEntityMapper = accountEntityMapper;
    }

    public boolean accountExists(String login) {
        var account = new Account();
        account.setLogin(login);
        return accountRepository.exists(account);
    }

    public void createAccount(AccountEntity accountEntity) {
        accountRepository.save(accountEntityMapper.toAccount(accountEntity, accountEntity.getPassword() != null
                ? hashPassword(accountEntity.getPassword()).get()
                : null));
    }

    public boolean login(AccountEntity accountEntity, String password) {
        return hashPassword(password).get().equals(accountEntity.getPassword());
    }

    public void deleteAccount(AccountEntity accountEntity) {
        accountRepository.delete(accountEntityMapper.toAccount(accountEntity, accountEntity.getPassword() != null
                ? hashPassword(accountEntity.getPassword()).get()
                : null));
    }

    public boolean passwordVerification(String pass1, String pass2) {
        return pass1 != null && pass1.equals(pass2);
    }

    public AccountEntity getByLogin(String login) {
        var account = new Account();
        account.setLogin(login);
        account = accountRepository.getByLogin(account);

        if (account != null) {
            return accountEntityMapper.toAccountEntity(account);
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
}
