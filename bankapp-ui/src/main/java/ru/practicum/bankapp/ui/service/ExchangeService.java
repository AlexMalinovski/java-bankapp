package ru.practicum.bankapp.ui.service;

import ru.practicum.bankapp.lib.dto.rate.RateDto;

import java.util.List;

public interface ExchangeService {
    List<RateDto> getCurrentRates();
}
