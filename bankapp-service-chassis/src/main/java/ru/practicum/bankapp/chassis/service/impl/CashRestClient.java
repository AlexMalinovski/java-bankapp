package ru.practicum.bankapp.chassis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;
import ru.practicum.bankapp.chassis.config.url.CashUrls;
import ru.practicum.bankapp.chassis.config.url.CustomHeaders;
import ru.practicum.bankapp.chassis.service.CashClient;
import ru.practicum.bankapp.lib.dto.cash.CashOperationDto;
import ru.practicum.bankapp.lib.dto.cash.OperationResult;

@RequiredArgsConstructor
public class CashRestClient implements CashClient {
    private final RestClient restClient;

    @Override
    public OperationResult executeCashOperation(String login, CashOperationDto cashOperationDto) {
        return restClient
                .post().uri(CashUrls.Operation.FULL)
                .header(CustomHeaders.USER_LOGIN, login)
                .body(cashOperationDto)
                .retrieve()
                .body(OperationResult.class);
    }
}
