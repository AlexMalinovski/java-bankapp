package ru.practicum.bankapp.exchange.service.impl;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.practicum.bankapp.exchange.entity.Rate;
import ru.practicum.bankapp.exchange.repository.RateRepository;
import ru.practicum.bankapp.exchange.service.ExchangeService;
import ru.practicum.bankapp.exchange.service.impl.mapper.RateMapper;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.dto.exchange.ExchangeDto;
import ru.practicum.bankapp.lib.dto.rate.RateDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeServiceImpl implements ExchangeService {
    static final Long RATE_TOLERANCE_S = 1L;
    private final RateRepository ratesRepository;
    private final RateMapper rateMapper;
    private final Validator validator;

    @Override
    public List<RateDto> getCurrentRates() {
        LocalDateTime now = LocalDateTime.now();

        return ratesRepository.findAllByTimeAfter(now.minusSeconds(RATE_TOLERANCE_S))
                .stream()
                .collect(Collectors.toMap(Rate::getCurrency, Function.identity(),
                        (old, cur) -> old.getTime().isBefore(cur.getTime()) ? cur : old))
                .values()
                .stream()
                .map(rateMapper::toRateDto)
                .toList();
    }

    @Override
    public Void addRates(Collection<RateDto> rateDto) {
        List<Rate> newRates = Optional.ofNullable(rateDto)
                .orElse(List.of())
                .stream()
                .filter(dto -> validator.validate(dto).isEmpty())
                .map(rateMapper::toRate)
                .toList();
        if (!newRates.isEmpty()) {
            ratesRepository.saveAll(newRates);
        }

        if (rateDto != null) {
            log.debug("Обновление курсов валют. Поступило:{} сохранено:{}", rateDto.size(), newRates.size());
        }
        return null;
    }

    @Override
    public ExchangeDto exchange(ExchangeDto request) {
        log.info("Расчет конверсии по запросу {}", request);
        Assert.notNull(request, "Аргумент не может быть null");

        var response = request.toBuilder();
        if (request.getValueFrom() != null) {
            response.valueTo(convertCurrencyFrom2CurrencyTo(request));
        } else {
            throw new IllegalStateException("Не реализовано");
        }
        return response.build();
    }

    protected BigDecimal convertCurrencyFrom2CurrencyTo(ExchangeDto request) {
        final Currency currencyFrom = request.getCurrencyFrom();
        final Currency currencyTo = request.getCurrencyTo();
        final BigDecimal valueFrom = request.getValueFrom();
        if (currencyFrom == currencyTo) {
            return valueFrom;
        }

        LocalDateTime now = LocalDateTime.now();
        List<Rate> currentRates = ratesRepository.findAllByCurrencyInAndTimeAfter(
                Set.of(currencyFrom, currencyTo), now.minusSeconds(RATE_TOLERANCE_S));

        Map<Currency, Rate> rates = Stream.concat(currentRates.stream(),
                        Stream.of(Rate.builder().currency(Currency.RUB).value(BigDecimal.ONE).time(now).build()))
                .collect(Collectors.toMap(Rate::getCurrency, Function.identity(),
                        (old, cur) -> old.getTime().isBefore(cur.getTime()) ? cur : old));


        Assert.state(rates.containsKey(currencyFrom) && rates.containsKey(currencyTo),
                "Отсутствует котировка требуемой валюты");

        return  (rates.get(currencyTo).getValue().multiply(valueFrom).setScale(2, RoundingMode.DOWN))
                .divide(rates.get(currencyFrom).getValue(), 2, RoundingMode.DOWN);
    }
}
