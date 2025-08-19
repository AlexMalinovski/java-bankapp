package ru.practicum.bankapp.exchange.service;

import ru.practicum.bankapp.lib.dto.exchange.ExchangeDto;
import ru.practicum.bankapp.lib.dto.rate.RateDto;

import java.util.Collection;
import java.util.List;

public interface ExchangeService {
    List<RateDto> getCurrentRates();

    Void addRates(Collection<RateDto> rateDto);

    ExchangeDto exchange(ExchangeDto request);
}
