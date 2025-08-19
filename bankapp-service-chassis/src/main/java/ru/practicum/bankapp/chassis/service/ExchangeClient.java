package ru.practicum.bankapp.chassis.service;

import ru.practicum.bankapp.lib.dto.exchange.ExchangeDto;
import ru.practicum.bankapp.lib.dto.rate.RateDto;

import java.util.List;

public interface ExchangeClient {
    List<RateDto> getCurrentRates();

    ExchangeDto exchange(ExchangeDto request);

    void addRates(List<RateDto> currentRates);
}
