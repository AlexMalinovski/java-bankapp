package ru.practicum.bankapp.ui.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class LoginControllerTestIT extends AbstractUiControllerTest {

    @Test
    @SneakyThrows
    void getLoginPage() {
        mockMvc.perform(get(UiUrls.LogIn.FULL))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
}