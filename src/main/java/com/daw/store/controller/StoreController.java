package com.daw.store.controller;

import com.daw.store.entity.AccountEntity;
import com.daw.store.entity.ItemEntity;
import com.daw.store.service.StoreService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController()
@Slf4j
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping("/api/get")
    public AccountEntity get(@NonNull @RequestParam String login) {
        log.info("api get for {}", login);
        return storeService.getByLogin(login);
    }

    @GetMapping("/api/get-item")
    public ItemEntity getItem(@NonNull @RequestParam String itemId) {
        log.info("api getItem {}", itemId);
        return storeService.getItem(itemId);
    }

    @PostMapping("/api/create")
    public AccountEntity create(@NonNull @RequestBody AccountEntity account) {
        var login = account.getLogin();
        log.info("api register for {}", login);
        if (!storeService.accountExists(login)) {
            storeService.createAccount(account);
        }
        return storeService.getByLogin(login);
    }

    @PostMapping("/api/update")
    public AccountEntity update(@NonNull @RequestBody AccountEntity account) {
        log.info("api update for {}", account.getLogin());
        var login = account.getLogin();
        if (storeService.accountExists(login)) {
            storeService.updateAccount(account);
            return storeService.getByLogin(account.getLogin());
        } else {
            return null;
        }
    }

    @DeleteMapping("/api/delete")
    public void delete(@NonNull @RequestParam String login) {
        log.info("api delete for {}", login);
        storeService.deleteAccount(login);
    }
}
