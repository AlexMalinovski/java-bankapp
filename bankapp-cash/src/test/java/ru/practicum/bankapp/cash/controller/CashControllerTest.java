package ru.practicum.bankapp.cash.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.bankapp.chassis.config.url.CashUrls;
import ru.practicum.bankapp.chassis.config.url.CustomHeaders;
import ru.practicum.bankapp.lib.common.constant.CashOperation;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.dto.cash.CashOperationDto;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CashControllerTest extends AbstractCashControllerTest {

    @Test
    @SneakyThrows
    void executeCashOperation() {
        CashOperationDto cashOperationDto = new CashOperationDto(Currency.RUB, CashOperation.PUT, BigDecimal.TEN);

        mockMvc.perform(post(CashUrls.Operation.FULL)
                        .header(CustomHeaders.USER_LOGIN, "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(cashOperationDto)))
                .andExpect(status().isOk());

        verify(cashService).executeCashOperation("login", cashOperationDto);
    }
}