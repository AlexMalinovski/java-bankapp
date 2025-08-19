package ru.practicum.bankapp.chassis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;
import ru.practicum.bankapp.chassis.config.url.CustomHeaders;
import ru.practicum.bankapp.chassis.config.url.TransferUrls;
import ru.practicum.bankapp.chassis.service.TransferClient;
import ru.practicum.bankapp.lib.dto.cash.OperationResult;
import ru.practicum.bankapp.lib.dto.transfer.MoneyTransferDto;

@RequiredArgsConstructor
public class TransferRestClient implements TransferClient {
    private final RestClient restClient;

    @Override
    public OperationResult transferMoney(String login, MoneyTransferDto transferDto) {
        return restClient
                .post().uri(TransferUrls.Transfer.FULL)
                .header(CustomHeaders.USER_LOGIN, login)
                .body(transferDto)
                .retrieve()
                .body(OperationResult.class);
    }
}
