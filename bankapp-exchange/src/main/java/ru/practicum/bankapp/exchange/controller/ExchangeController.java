package ru.practicum.bankapp.exchange.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.bankapp.chassis.config.url.ExchangeUrls;
import ru.practicum.bankapp.exchange.service.ExchangeService;
import ru.practicum.bankapp.lib.dto.exchange.ExchangeDto;
import ru.practicum.bankapp.lib.dto.rate.RateDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExchangeController {
    private final ExchangeService exchangeService;

    @GetMapping(ExchangeUrls.Rates.FULL)
    ResponseEntity<List<RateDto>> getCurrentRates() {
        return ResponseEntity.ok(exchangeService.getCurrentRates());
    }

    @PostMapping(ExchangeUrls.Rates.FULL)
    ResponseEntity<Void> addRates(@RequestBody List<RateDto> rateDto) {
        return ResponseEntity.ok(exchangeService.addRates(rateDto));
    }

    @PostMapping(ExchangeUrls.Exchange.FULL)
    ResponseEntity<ExchangeDto> exchange(@RequestBody @Valid ExchangeDto request) {
        return ResponseEntity.ok(exchangeService.exchange(request));
    }
}
