package ru.practicum.bankapp.ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {
    @GetMapping(path = UiUrls.LogIn.FULL)
    public String getLoginPage() {
        return "login";
    }

    @GetMapping(path = UiUrls.LogOut.FULL)
    public String getLogoutPage() {
        return "logout";
    }
}
