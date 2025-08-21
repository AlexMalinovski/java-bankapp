package ru.practicum.bankapp.exchangegen.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.practicum.bankapp.chassis.service.ExchangeClient;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.dto.rate.RateDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeGeneratorImpl {
    private static final int RATE_PRECISION = 6;
    private final ExchangeClient exchangeClient;

    protected List<RateDto> getCurrentRates() {
        double rubUsd = 1.0 / ThreadLocalRandom.current().nextDouble(75.0, 76.0);
        double rubCny = 1.0 / ThreadLocalRandom.current().nextDouble(12.0, 13.0);
        LocalDateTime now = LocalDateTime.now();
        return List.of(
                new RateDto(Currency.USD.getTitle(), Currency.USD.name(),
                        BigDecimal.valueOf(rubUsd).setScale(RATE_PRECISION, RoundingMode.HALF_EVEN), now),
                new RateDto(Currency.CNY.getTitle(), Currency.CNY.name(),
                        BigDecimal.valueOf(rubCny).setScale(RATE_PRECISION, RoundingMode.HALF_EVEN), now)
        );
    }

    @Scheduled(fixedRate = 1000)
    public void uploadNewRates() {
        exchangeClient.addRates(getCurrentRates());
        log.info("Отправлены обновления курсов валют");
    }
}
