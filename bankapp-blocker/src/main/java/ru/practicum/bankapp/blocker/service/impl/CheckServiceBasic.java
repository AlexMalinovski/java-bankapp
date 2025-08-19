package ru.practicum.bankapp.blocker.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.bankapp.blocker.service.CheckService;
import ru.practicum.bankapp.lib.dto.blocker.BlockerRequest;
import ru.practicum.bankapp.lib.dto.blocker.BlockerResponse;

import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class CheckServiceBasic implements CheckService {
    @Override
    public BlockerResponse checkOperation(BlockerRequest blockerRequest) {
        var builder = BlockerResponse.builder();
        if (ThreadLocalRandom.current().nextInt(100) >= 80) {
            builder.accepted(false).message("Очень подозрительная операция");
            log.warn("Обнаружена подозрительная операция: {}", blockerRequest);
        } else {
            builder.accepted(true);
            log.info("Операция валидирована: {}", blockerRequest);
        }
        return builder.build();
    }
}
