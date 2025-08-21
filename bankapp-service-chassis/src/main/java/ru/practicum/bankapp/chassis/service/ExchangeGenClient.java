package ru.practicum.bankapp.chassis.service;

import ru.practicum.bankapp.lib.dto.rate.RateDto;

import java.util.List;

public interface ExchangeGenClient {
    List<RateDto> getCurrentRates();
}
