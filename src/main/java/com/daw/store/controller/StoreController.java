package com.daw.store.controller;

import com.daw.store.entity.AccountEntity;
import com.daw.store.service.AccountService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController()
@Slf4j
public class StoreController {

    private final AccountService accountService;

    public StoreController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/api/get")
    public AccountEntity get(@NonNull @RequestParam String login) {
        log.info("api get for {}", login);
        var account = new AccountEntity();
        account.setLogin(login);
        return accountService.getByLogin(account.getLogin());
    }

    @PostMapping("/api/create")
    public AccountEntity create(@NonNull @RequestBody AccountEntity account) {
        log.info("api register for {}", account.getLogin());
        var login = account.getLogin();
        if (accountService.passwordVerification(account.getPassword(), account.getPasswordConfirmed())
                && !accountService.accountExists(login)) {
            accountService.createAccount(account);
            return accountService.getByLogin(account.getLogin());
        } else {
            return null;
        }
    }

    @PostMapping("/api/update")
    public AccountEntity update(@NonNull @RequestBody AccountEntity account) {
        log.info("api update for {}", account.getLogin());
        var login = account.getLogin();
        if (accountService.accountExists(login)) {
            accountService.updateAccount(account);
            return accountService.getByLogin(account.getLogin());
        } else {
            return null;
        }
    }

    @DeleteMapping("/api/delete")
    public int delete(@NonNull @RequestParam String login) {
        log.info("api delete for {}", login);
        var account = new AccountEntity();
        account.setLogin(login);
        accountService.deleteAccount(account);
        return 200;
    }
}
