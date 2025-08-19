package ru.practicum.bankapp.ui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.bankapp.lib.dto.rate.RateDto;
import ru.practicum.bankapp.ui.service.ExchangeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final ExchangeService exchangeService;

    @GetMapping(ApiUrls.Rates.FULL)
    public ResponseEntity<List<RateDto>> getRate() {
        List<RateDto> ras = exchangeService.getCurrentRates();
        return ResponseEntity.ok(ras);
    }
}
