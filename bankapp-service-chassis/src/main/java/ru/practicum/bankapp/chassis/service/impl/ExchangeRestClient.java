package ru.practicum.bankapp.chassis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import ru.practicum.bankapp.chassis.config.url.ExchangeUrls;
import ru.practicum.bankapp.chassis.service.ExchangeClient;
import ru.practicum.bankapp.lib.dto.exchange.ExchangeDto;
import ru.practicum.bankapp.lib.dto.rate.RateDto;

import java.util.List;

@RequiredArgsConstructor
public class ExchangeRestClient implements ExchangeClient {
    private final RestClient restClient;

    @Override
    public List<RateDto> getCurrentRates() {
        return restClient
                .get()
                .uri(ExchangeUrls.Rates.FULL)
                .retrieve().body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public ExchangeDto exchange(ExchangeDto request) {
        return restClient.post()
                .uri(ExchangeUrls.Exchange.FULL)
                .body(request)
                .retrieve()
                .body(ExchangeDto.class);
    }

    @Override
    public void addRates(List<RateDto> currentRates) {
        restClient.post()
                .uri(ExchangeUrls.Rates.FULL)
                .body(currentRates)
                .retrieve()
                .body(Void.class);
    }
}
