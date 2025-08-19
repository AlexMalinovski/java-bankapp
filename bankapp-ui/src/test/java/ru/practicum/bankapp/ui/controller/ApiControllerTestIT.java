package ru.practicum.bankapp.ui.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ApiControllerTestIT extends AbstractUiControllerTest {

    @Test
    @SneakyThrows
    void getRate() {
        mockMvc.perform(get(ApiUrls.Rates.FULL))
                .andExpect(status().isOk());
    }
}