package ru.practicum.bankapp.ui.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.bankapp.chassis.service.ExchangeClient;
import ru.practicum.bankapp.lib.dto.rate.RateDto;
import ru.practicum.bankapp.ui.service.ExchangeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {
    private final ExchangeClient exchangeClient;

    @Override
    public List<RateDto> getCurrentRates() {
        return exchangeClient.getCurrentRates();
    }
}
