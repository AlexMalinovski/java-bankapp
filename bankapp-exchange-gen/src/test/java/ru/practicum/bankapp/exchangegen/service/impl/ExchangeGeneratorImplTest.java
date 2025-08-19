package ru.practicum.bankapp.exchangegen.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.bankapp.chassis.service.ExchangeClient;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.dto.rate.RateDto;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeGeneratorImplTest {

    @Mock
    private ExchangeClient exchangeClient;

    @InjectMocks
    @Spy
    private ExchangeGeneratorImpl exchangeGenerator;

    @Test
    void uploadNewRates() {
        List<RateDto> rates = List.of();
        when(exchangeGenerator.getCurrentRates()).thenReturn(rates);

        exchangeGenerator.uploadNewRates();

        verify(exchangeClient).addRates(rates);
    }

    @Test
    void getCurrentRates() {
        var currentRates = exchangeGenerator.getCurrentRates()
                .stream()
                .collect(Collectors.toMap(RateDto::getName, Function.identity()));

        Set<String> expected = Currency.ALL_NAMES.stream()
                .filter(name -> !"RUB".equals(name))
                .collect(Collectors.toSet());

        assertEquals(expected, currentRates.keySet());
    }
}