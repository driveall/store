package com.daw.store.controller;

import com.daw.store.entity.AccountEntity;
import com.daw.store.service.AccountService;
import com.daw.store.service.ValidationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;

import static com.daw.store.Constants.*;
import static com.daw.store.Constants.ATTRIBUTE_LOGIN;

@EnableWebMvc
@Controller
@Slf4j
public class ViewController {
    private final AccountService accountService;
    private final ValidationService validationService;
    private final RestTemplate restTemplate = new RestTemplate();

    public ViewController(AccountService accountService, ValidationService validationService) {
        this.accountService = accountService;
        this.validationService = validationService;
    }

    @RequestMapping("/index")
    public ModelAndView getIndex() {
        log.info("index get");
        return new ModelAndView("index");
    }

    @GetMapping("/login")
    public ModelAndView getLogin() {
        log.info("login get");
        return new ModelAndView("login");
    }

    @GetMapping("/register")
    public ModelAndView getRegister() {
        log.info("register get");
        return new ModelAndView("register");
    }

    @GetMapping("/success")
    public ModelAndView getSuccess(HttpServletRequest req,
                                   HttpServletResponse resp) throws IOException {
        log.info("success get");
        if (getSessionAttribute(req) != null) {
            var account = accountService.getByLogin(getSessionAttribute(req));
            var mav = new ModelAndView("main");
            mav.addObject("account", account);
            return mav;
        }
        resp.sendRedirect(INDEX_PAGE_PATH);
        return null;
    }

    @GetMapping("/update")
    public ModelAndView getUpdate(HttpServletRequest req,
                                  HttpServletResponse resp) throws IOException {
        log.info("update get");
        if (getSessionAttribute(req) != null) {
            var account = accountService.getByLogin(getSessionAttribute(req));
            var mav = new ModelAndView("update");
            mav.addObject("account", account);
            return mav;
        }
        resp.sendRedirect(INDEX_PAGE_PATH);
        return null;
    }

    @PostMapping("/login")
    public void login(@NonNull @RequestParam String login,
                      @NonNull @RequestParam String pass,
                      HttpServletRequest req,
                      HttpServletResponse resp) {
        log.info("login for {}", login);

        var response = restTemplate.getForEntity(String.format(API_GET_URL, login), AccountEntity.class);

        var accountEntity = response.getBody();
        if (accountEntity != null && accountService.login(accountEntity, pass)) {
            addSessionAttribute(req, response.getBody().getLogin());
            redirect(resp, SUCCESS_PAGE_PATH);
        } else {
            redirect(resp, INDEX_PAGE_PATH);
        }
    }

    @PostMapping("/register")
    public void register(@NonNull @RequestParam String login,
                         @NonNull @RequestParam String pass,
                         @NonNull @RequestParam String pass2,
                         @Nullable @RequestParam(required = false) String email,
                         @Nullable @RequestParam(required = false) String phone,
                         HttpServletRequest req,
                         HttpServletResponse resp) {
        log.info("register for {}", login);
        var accountEntity = new AccountEntity();
        accountEntity.setLogin(login);
        accountEntity.setPassword(pass);
        accountEntity.setPasswordConfirmed(pass2);
        if (!email.isEmpty() && validationService.validateEmailAddress(email)) {
            accountEntity.setEmail(email);
        }
        if (!phone.isEmpty() && validationService.validatePhoneNumber(phone)) {
            accountEntity.setPhone(phone);
        }

        var response = restTemplate.postForEntity(API_CREATE_URL, accountEntity, AccountEntity.class);

        if (response.getBody() != null) {
            addSessionAttribute(req, response.getBody().getLogin());
            redirect(resp, SUCCESS_PAGE_PATH);
        } else {
            redirect(resp, INDEX_PAGE_PATH);
        }
    }

    @PostMapping("/update")
    public void update(@NonNull @RequestParam String login,
                       @NonNull @RequestParam String pass,
                       @NonNull @RequestParam String pass2,
                       @NonNull @RequestParam String email,
                       @NonNull @RequestParam String phone,
                       HttpServletRequest req,
                       HttpServletResponse resp) {
        log.info("update for {}", login);
        var accountEntity = new AccountEntity();
        accountEntity.setLogin(login);
        if (!pass.isEmpty()) {
            accountEntity.setPassword(pass);
            accountEntity.setPasswordConfirmed(pass2);
        }
        if (!email.isEmpty() && validationService.validateEmailAddress(email)) {
            accountEntity.setEmail(email);
        }
        if (!phone.isEmpty() && validationService.validatePhoneNumber(phone)) {
            accountEntity.setPhone(validationService.formatPhoneNumber(phone));
        }

        var response = restTemplate.postForEntity(API_UPDATE_URL, accountEntity, AccountEntity.class);

        if (response.getBody() != null) {
            addSessionAttribute(req, response.getBody().getLogin());
            redirect(resp, SUCCESS_PAGE_PATH);
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

    @PostMapping("/delete")
    public void delete(@RequestParam String login,
                       HttpServletRequest req,
                       HttpServletResponse resp) {
        log.info("delete for {}", login);
        removeSessionAttribute(req);

        restTemplate.delete(String.format(API_DELETE_URL, login));

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
