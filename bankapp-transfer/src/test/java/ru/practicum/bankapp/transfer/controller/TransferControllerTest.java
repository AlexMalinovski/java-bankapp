package ru.practicum.bankapp.transfer.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.bankapp.chassis.config.url.CustomHeaders;
import ru.practicum.bankapp.chassis.config.url.TransferUrls;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.dto.transfer.MoneyTransferDto;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransferControllerTest extends AbstractTransferControllerTest {

    @Test
    @SneakyThrows
    void transferMoney() {
        var dto = MoneyTransferDto.builder()
                .currencyFrom(Currency.RUB).currencyTo(Currency.RUB)
                .value(BigDecimal.TEN)
                .loginTo("to")
                .build();

        mockMvc.perform(post(TransferUrls.Transfer.FULL)
                        .header(CustomHeaders.USER_LOGIN, "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk());

        verify(transferService).transferMoney("login", dto);
    }
}