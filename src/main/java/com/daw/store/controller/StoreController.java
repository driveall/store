package com.daw.store.controller;

import com.daw.store.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Slf4j
public class StoreController {

    private final AccountService accountService;

    public StoreController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/login")
    public void login(@RequestParam String login,
                      @RequestParam String pass,
                      HttpServletRequest req,
                      HttpServletResponse resp) throws IOException {
        log.info("login for " + login);
        if (accountService.accountExists(login) && accountService.login(login, pass)) {
            req.getSession().setAttribute("login", login);
            resp.sendRedirect("/success");
        } else {
            resp.sendRedirect("/index.html");
        }
    }

    @PostMapping("/register")
    public void register(@RequestParam String login,
                         @RequestParam String pass,
                         @RequestParam String pass2,
                         HttpServletRequest req,
                         HttpServletResponse resp) throws IOException {
        log.info("register for " + login);
        if (accountService.passwordConfirmed(pass, pass2)
                && !accountService.accountExists(login)) {
            accountService.createAccount(login, pass);
            req.getSession().setAttribute("login", login);
            resp.sendRedirect("/success");
        } else {
            resp.sendRedirect("/index.html");
        }
    }

    @PostMapping("/unlogin")
    public void unlogin(HttpServletRequest req,
                        HttpServletResponse resp) throws IOException {
        log.info("unlogin for " + req.getSession().getAttribute("login"));
        req.getSession().removeAttribute("login");
        resp.sendRedirect("/index.html");
    }

    @GetMapping("/success")
    public String success(HttpServletRequest req,
                        HttpServletResponse resp) throws IOException {
        var login = (String) req.getSession().getAttribute("login");
        log.info("success for " + login);
        if (login != null) {
            return String.format("""
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta charset="UTF-8">
                        <link rel="stylesheet" href="styles.css">
                    </head>
                    <body>
                    <div class="center470">
                    <h1>Login Successful for %s</h1>
                    <form action="/delete" method="post">
                        <input type="hidden" name="login" value="%s">
                        <input type="submit" value="Delete" class="btn200" >
                    </form>
                    <form action="/unlogin" method="post">
                        <input type="submit" value="Unlogin" class="btn200" >
                    </form>
                    </div>
                    </body>
                    </html>
                    """, login, login);
        } else {
            resp.sendRedirect("/index.html");
            return null;
        }
    }

    @PostMapping("/delete")
    public void delete(@RequestParam String login,
                       HttpServletRequest req,
                       HttpServletResponse resp) throws IOException {
        log.info("delete for " + login);
        req.getSession().removeAttribute("login");
        accountService.deleteAccount(login);
        resp.sendRedirect("/index.html");
    }
}
