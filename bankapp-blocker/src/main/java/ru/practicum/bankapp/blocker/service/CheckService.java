package ru.practicum.bankapp.blocker.service;

import ru.practicum.bankapp.lib.dto.blocker.BlockerRequest;
import ru.practicum.bankapp.lib.dto.blocker.BlockerResponse;

public interface CheckService {
    BlockerResponse checkOperation(BlockerRequest blockerRequest);
}
