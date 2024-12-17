package com.daw.store.controller;

import com.daw.store.entity.AccountEntity;
import com.daw.store.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static com.daw.store.Constants.*;
import static com.daw.store.Constants.ATTRIBUTE_LOGIN;

@RestController
@Slf4j
public class ViewController {
    private final AccountService accountService;
    private final RestTemplate restTemplate = new RestTemplate();

    public ViewController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/login")
    public void login(@RequestParam String login,
                      @RequestParam String pass,
                      HttpServletRequest req,
                      HttpServletResponse resp) {
        log.info("login for {}", login);

        var accountEntity = new AccountEntity();
        accountEntity.setLogin(login);
        accountEntity.setPassword(pass);

        var response = restTemplate.getForEntity(String.format(API_GET_URL, login), AccountEntity.class);

        if (response.getBody() != null) {
            accountEntity = response.getBody();
            if (accountService.login(accountEntity)) {
                addSessionAttribute(req, response.getBody().getLogin());
                redirect(resp, SUCCESS_PAGE_PATH);
            } else {
                redirect(resp, INDEX_PAGE_PATH);
            }
        } else {
            redirect(resp, INDEX_PAGE_PATH);
        }
    }

    @PostMapping("/register")
    public void register(@RequestParam String login,
                         @RequestParam String pass,
                         @RequestParam String pass2,
                         HttpServletRequest req,
                         HttpServletResponse resp) {
        log.info("register for {}", login);
        if (accountService.passwordVerification(pass, pass2)
                && accountService.accountExists(login)) {
            var accountEntity = new AccountEntity();
            accountEntity.setLogin(login);
            accountEntity.setPassword(pass);
            accountEntity.setPasswordConfirmed(pass2);

            var response = restTemplate.postForEntity(API_CREATE_URL, accountEntity, AccountEntity.class);

            if (response.getBody() != null) {
                addSessionAttribute(req, response.getBody().getLogin());
                redirect(resp, SUCCESS_PAGE_PATH);
            } else {
                redirect(resp, INDEX_PAGE_PATH);
            }
        } else {
            redirect(resp, INDEX_PAGE_PATH);
        }
    }

    @PostMapping("/unlogin")
    public void unlogin(HttpServletRequest req,
                        HttpServletResponse resp) {
        log.info("unlogin for {}", req.getSession().getAttribute(ATTRIBUTE_LOGIN));
        removeSessionAttribute(req);
        redirect(resp, INDEX_PAGE_PATH);
    }

    @GetMapping("/success")
    public String success(HttpServletRequest req,
                          HttpServletResponse resp) {
        var login = getSessionAttribute(req);
        log.info("success for {}", login);
        if (login != null) {
            return String.format(SUCCESS_PAGE_CONTENT, login, ATTRIBUTE_LOGIN, login);
        } else {
            redirect(resp, INDEX_PAGE_PATH);
            return null;
        }
    }

    @PostMapping("/delete")
    public void delete(@RequestParam String login,
                       HttpServletRequest req,
                       HttpServletResponse resp) {
        log.info("delete for {}", login);
        removeSessionAttribute(req);

        var accountEntity = new AccountEntity();
        accountEntity.setLogin(login);

        restTemplate.delete(String.format(API_DELETE_URL, login), accountEntity);

        redirect(resp, INDEX_PAGE_PATH);
    }

    private void addSessionAttribute(HttpServletRequest req, String login) {
        req.getSession().setAttribute(ATTRIBUTE_LOGIN, login);
    }
    private void removeSessionAttribute(HttpServletRequest req) {
        req.getSession().removeAttribute(ATTRIBUTE_LOGIN);
    }
    private String getSessionAttribute(HttpServletRequest req) {
        return (String) req.getSession().getAttribute(ATTRIBUTE_LOGIN);
    }

    private void redirect(HttpServletResponse resp, String path) {
        try {
            resp.sendRedirect(path);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
