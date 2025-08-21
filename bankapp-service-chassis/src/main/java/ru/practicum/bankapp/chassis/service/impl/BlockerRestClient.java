package ru.practicum.bankapp.chassis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;
import ru.practicum.bankapp.chassis.config.url.BlockerUrls;
import ru.practicum.bankapp.chassis.service.BlockerClient;
import ru.practicum.bankapp.lib.dto.blocker.BlockerRequest;
import ru.practicum.bankapp.lib.dto.blocker.BlockerResponse;

@RequiredArgsConstructor
public class BlockerRestClient implements BlockerClient {
    private final RestClient restClient;

    @Override
    public BlockerResponse checkTransaction(BlockerRequest blockerRequest) {
        return restClient.post()
                .uri(BlockerUrls.Checker.FULL)
                .body(blockerRequest)
                .retrieve()
                .body(BlockerResponse.class);
    }
}
