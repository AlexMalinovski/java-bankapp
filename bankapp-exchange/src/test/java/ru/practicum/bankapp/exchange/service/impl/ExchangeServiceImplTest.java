package ru.practicum.bankapp.exchange.service.impl;

import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.bankapp.exchange.entity.Rate;
import ru.practicum.bankapp.exchange.repository.RateRepository;
import ru.practicum.bankapp.exchange.service.impl.mapper.RateMapper;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.dto.exchange.ExchangeDto;
import ru.practicum.bankapp.lib.dto.rate.RateDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeServiceImplTest {

    @Mock
    private RateRepository ratesRepository;

    @Mock
    private RateMapper rateMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private ExchangeServiceImpl exchangeService;

    @Test
    void getCurrentRates_returnLatestRate() {
        Rate rateOld = Rate.builder()
                .currency(Currency.USD).value(BigDecimal.ONE)
                .time(LocalDateTime.now())
                .build();

        Rate rateLast = Rate.builder()
                .currency(Currency.USD).value(BigDecimal.ONE)
                .time(LocalDateTime.now())
                .build();

        RateDto expected = new RateDto();
        when(ratesRepository.findAllByTimeAfter(any())).thenReturn(List.of(rateLast, rateOld));
        when(rateMapper.toRateDto(rateLast)).thenReturn(expected);

        List<RateDto> actual = exchangeService.getCurrentRates();

        assertEquals(1, actual.size());
        assertEquals(expected, actual.get(0));
    }

    @Test
    void addRates() {
        RateDto rateDto = new RateDto();
        Rate rate = new Rate();

        when(validator.validate(rateDto)).thenReturn(Collections.EMPTY_SET);
        when(rateMapper.toRate(rateDto)).thenReturn(rate);

        exchangeService.addRates(List.of(rateDto));

        verify(ratesRepository).saveAll(List.of(rate));
    }

    @Test
    void exchange_whenValueFrom() {
        ExchangeDto request = ExchangeDto.builder()
                .currencyFrom(Currency.RUB).currencyTo(Currency.USD)
                .valueFrom(BigDecimal.TEN)
                .build();

        Rate rate = Rate.builder()
                .currency(Currency.USD)
                .value(BigDecimal.valueOf(10L, 2))
                .time(LocalDateTime.now())
                .build();

        when(ratesRepository.findAllByCurrencyInAndTimeAfter(any(), any())).thenReturn(List.of(rate));

        ExchangeDto exchange = exchangeService.exchange(request);

        assertEquals(0, BigDecimal.ONE.compareTo(exchange.getValueTo()));
        assertEquals(request.getValueFrom(), exchange.getValueFrom());
        assertEquals(request.getCurrencyFrom(), exchange.getCurrencyFrom());
        assertEquals(request.getCurrencyTo(), exchange.getCurrencyTo());
    }
}