package ru.practicum.bankapp.cash.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.bankapp.cash.service.CashService;
import ru.practicum.bankapp.chassis.config.url.CashUrls;
import ru.practicum.bankapp.chassis.config.url.CustomHeaders;
import ru.practicum.bankapp.lib.dto.cash.CashOperationDto;
import ru.practicum.bankapp.lib.dto.cash.OperationResult;

@RestController
@RequiredArgsConstructor
public class CashController {

    private final CashService cashService;

    @PostMapping(CashUrls.Operation.FULL)
    public ResponseEntity<OperationResult> executeCashOperation(
            @RequestBody @Valid CashOperationDto cashOperationDto,
            @RequestHeader(CustomHeaders.USER_LOGIN) String login) {

        return ResponseEntity.ok(cashService.executeCashOperation(login, cashOperationDto));
    }
}
