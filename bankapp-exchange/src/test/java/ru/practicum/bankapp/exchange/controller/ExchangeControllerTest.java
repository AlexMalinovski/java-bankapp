package ru.practicum.bankapp.exchange.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.bankapp.chassis.config.url.ExchangeUrls;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.dto.exchange.ExchangeDto;
import ru.practicum.bankapp.lib.dto.rate.RateDto;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExchangeControllerTest extends AbstractExchangeControllerTest {

    @Test
    @SneakyThrows
    void getCurrentRates() {
        mockMvc.perform(get(ExchangeUrls.Rates.FULL))
                .andExpect(status().isOk());

        verify(exchangeService).getCurrentRates();
    }

    @Test
    @SneakyThrows
    void addRates() {
        List<RateDto> rateDto = List.of();

        mockMvc.perform(post(ExchangeUrls.Rates.FULL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(rateDto)))
                .andExpect(status().isOk());

        verify(exchangeService).addRates(rateDto);
    }

    @Test
    @SneakyThrows
    void exchange() {
        ExchangeDto request = ExchangeDto.builder()
                .currencyFrom(Currency.RUB).currencyTo(Currency.USD)
                .valueFrom(BigDecimal.TEN)
                .build();

        mockMvc.perform(post(ExchangeUrls.Exchange.FULL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andExpect(status().isOk());

        verify(exchangeService).exchange(request);
    }
}