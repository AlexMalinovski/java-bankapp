package ru.practicum.bankapp.ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.practicum.bankapp.lib.dto.accounts.SignUpDto;
import ru.practicum.bankapp.lib.dto.cash.OperationResult;
import ru.practicum.bankapp.ui.service.AccountServiceAdapter;

@Controller
@RequiredArgsConstructor
public class SignUpController {
    private final AccountServiceAdapter accountServiceAdapter;

    @ModelAttribute("signUpResult")
    public OperationResult initEmptyResult() {
        return new OperationResult(null, null);
    }

    @GetMapping(path = UiUrls.SignUp.FULL)
    public String getSignUpPage(Model model) {
        model.addAttribute("signUpDto", new SignUpDto());

        return "signup";
    }

    @PostMapping(path = UiUrls.SignUp.FULL)
    public String signUpUser(@ModelAttribute @Validated SignUpDto signUpDto,
                             BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("signUpDto", signUpDto);
            return "signup";
        }

        OperationResult signUpResult = accountServiceAdapter.registerNewUser(signUpDto);
        if (!signUpResult.success()) {
            model.addAttribute("signUpResult", signUpResult);
            return "signup";
        }

        return "redirect:" + UiUrls.Main.FULL;
    }
}
