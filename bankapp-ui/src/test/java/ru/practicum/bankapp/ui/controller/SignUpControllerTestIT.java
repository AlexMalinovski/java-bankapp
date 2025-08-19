package ru.practicum.bankapp.ui.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import ru.practicum.bankapp.lib.dto.accounts.SignUpDto;
import ru.practicum.bankapp.lib.dto.cash.OperationResult;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class SignUpControllerTestIT extends AbstractUiControllerTest {

    @Test
    @SneakyThrows
    void getSignUpPage() {
        mockMvc.perform(get(UiUrls.SignUp.FULL))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }

    @Test
    @SneakyThrows
    void signUpUser() {
        SignUpDto signUpDto = new SignUpDto(
                "login", "pass", "pass", "name", LocalDate.now().minusYears(18));
        when(accountServiceAdapter.registerNewUser(signUpDto)).thenReturn(new OperationResult(true, null));

        mockMvc.perform(post(UiUrls.SignUp.FULL)
                        .param("login", "login")
                        .param("password", "pass")
                        .param("passwordConfirm", "pass")
                        .param("name", "name")
                        .param("birthdate", LocalDate.now().minusYears(18).toString())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(UiUrls.Main.FULL));
    }
}