package com.daw.store.controller;

import com.daw.store.entity.AccountEntity;
import com.daw.store.entity.Accounts;
import com.daw.store.entity.ItemEntity;
import com.daw.store.entity.Items;
import com.daw.store.service.StoreService;
import com.daw.store.service.ValidationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController()
@Slf4j
public class StoreController {

    private final StoreService storeService;
    private final ValidationService validationService;

    public StoreController(StoreService storeService, ValidationService validationService) {
        this.storeService = storeService;
        this.validationService = validationService;
    }

    @GetMapping("/api/get")
    public AccountEntity get(@NonNull @RequestParam String login) {
        log.info("api get for {}", login);
        return storeService.getByLogin(login);
    }

    @GetMapping("/api/get-all")
    public Accounts getAll() {
        log.info("api getAll");
        return Accounts.builder()
                .accounts(storeService.getAllAccounts())
                .build();
    }

    @GetMapping("/api/get-item")
    public ItemEntity getItem(@NonNull @RequestParam String itemId) {
        log.info("api getItem {}", itemId);
        return storeService.getItem(itemId);
    }

    @GetMapping("/api/get-all-items")
    public Items getAllItems() {
        log.info("api getAllItems");
        return Items.builder()
                .items(storeService.getAllItems())
                .build();
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
        if (account.getPassword() != null && !account.getPassword().isEmpty()
                && account.getPassword().equals(account.getPasswordConfirmed())) {
            account.setPassword(storeService.hashPassword(account.getPassword()).get());
            account.setPasswordConfirmed(null);
        }
        if (account.getEmail() == null || account.getEmail().isEmpty()
                || !validationService.validateEmailAddress(account.getEmail())) {
            account.setEmail(null);
        }
        if (account.getPhone() == null || account.getPhone().isEmpty()
                || !validationService.validatePhoneNumber(account.getPhone())) {
            account.setPhone(validationService.formatPhoneNumber(account.getPhone()));
        }
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

    @PostMapping("/api/login")
    public boolean login(@NonNull @RequestParam String login,
                      @NonNull @RequestParam String password) {
        log.info("api login for {}", login);
        var accountEntity = storeService.getByLogin(login);
        return accountEntity != null && storeService.login(accountEntity, password);
    }

    @PostMapping("/api/register")
    public void register(@NonNull @RequestParam String login,
                         @NonNull @RequestParam String pass,
                         @NonNull @RequestParam String pass2,
                         @Nullable @RequestParam(required = false) String email,
                         @Nullable @RequestParam(required = false) String phone,
                         HttpServletRequest req,
                         HttpServletResponse resp) {
        log.info("api register for {}", login);
        var accountEntity = AccountEntity.builder()
                .login(login)
                .password(pass)
                .passwordConfirmed(pass2)
                .build();
        if (email != null && !email.isEmpty() && validationService.validateEmailAddress(email)) {
            accountEntity.setEmail(email);
        }
        if (phone != null && !phone.isEmpty() && validationService.validatePhoneNumber(phone)) {
            accountEntity.setPhone(validationService.formatPhoneNumber(phone));
        }
        storeService.createAccount(accountEntity);
    }

    @PostMapping("/api/update-profile")
    public void update(@NonNull @RequestParam String login,
                       @NonNull @RequestParam String pass,
                       @NonNull @RequestParam String pass2,
                       @NonNull @RequestParam String email,
                       @NonNull @RequestParam String phone) {
        log.info("api update for {}", login);
        var accountEntity = AccountEntity.builder()
                .login(login)
                .build();
        if (!pass.isEmpty() && pass.equals(pass2)) {
            accountEntity.setPassword(pass);
            accountEntity.setPasswordConfirmed(pass2);
        }
        if (!email.isEmpty() && validationService.validateEmailAddress(email)) {
            accountEntity.setEmail(email);
        }
        if (!phone.isEmpty() && validationService.validatePhoneNumber(phone)) {
            accountEntity.setPhone(validationService.formatPhoneNumber(phone));
        }

        storeService.updateAccount(accountEntity);

    }

    @PostMapping("/api/buy")
    public boolean buy(@RequestParam String itemId,
                    @RequestParam String login) {
        log.info("api buy {} for {}", itemId, login);
        storeService.buy(login, itemId);
        return true;
    }

    @PostMapping("/api/sell")
    public boolean sell(@RequestParam String itemId,
                     @RequestParam String login) {
        log.info("sell {} for {}", itemId, login);
        storeService.sell(login, itemId);
        return true;
    }

    @PostMapping("/api/wear")
    public boolean wear(@RequestParam String itemId,
                     @RequestParam String login) {
        log.info("wear {} for {}", itemId, login);
        storeService.wear(login, itemId);
        return true;
    }

    @PostMapping("/api/unwear")
    public boolean unwear(@RequestParam String itemId,
                       @RequestParam String login) {
        log.info("unwear {} for {}", itemId, login);
        storeService.unwear(login, itemId);
        return true;
    }
}
