package ru.practicum.bankapp.chassis.service;

import ru.practicum.bankapp.lib.dto.blocker.BlockerRequest;
import ru.practicum.bankapp.lib.dto.blocker.BlockerResponse;

public interface BlockerClient {
    BlockerResponse checkTransaction(BlockerRequest blockerRequest);
}
