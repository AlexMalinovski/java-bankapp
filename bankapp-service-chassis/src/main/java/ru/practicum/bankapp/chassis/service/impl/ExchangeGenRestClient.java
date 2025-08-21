package ru.practicum.bankapp.chassis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import ru.practicum.bankapp.chassis.config.url.ExchangeGenUrls;
import ru.practicum.bankapp.chassis.service.ExchangeGenClient;
import ru.practicum.bankapp.lib.dto.rate.RateDto;

import java.util.List;

@RequiredArgsConstructor
public class ExchangeGenRestClient implements ExchangeGenClient {
    private final RestClient restClient;

    @Override
    public List<RateDto> getCurrentRates() {
        return restClient
                .get()
                .uri(ExchangeGenUrls.Current.FULL)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }
}
