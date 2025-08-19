package ru.practicum.bankapp.exchange.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.bankapp.exchange.AbstractIntegrationExchangeTest;
import ru.practicum.bankapp.exchange.entity.id.RateId;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.dto.rate.RateDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExchangeServiceImplTestIT extends AbstractIntegrationExchangeTest {

    @Test
    @Transactional
    void addRates() {
        RateDto rateDto = new RateDto(null, "USD", BigDecimal.ONE, LocalDateTime.now());

        exchangeService.addRates(List.of(rateDto));

        var rateOpt = ratesRepository.findById(new RateId(rateDto.getTime(), Currency.USD));
        assertTrue(rateOpt.isPresent());
        assertEquals(rateDto.getValue(), rateOpt.get().getValue());
    }

    @Test
    void exchange() {
    }
}