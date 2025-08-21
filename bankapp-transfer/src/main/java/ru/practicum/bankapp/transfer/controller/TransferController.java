package ru.practicum.bankapp.transfer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.bankapp.chassis.config.url.CustomHeaders;
import ru.practicum.bankapp.chassis.config.url.TransferUrls;
import ru.practicum.bankapp.lib.dto.cash.OperationResult;
import ru.practicum.bankapp.lib.dto.transfer.MoneyTransferDto;
import ru.practicum.bankapp.transfer.service.TransferService;

@RestController
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping(TransferUrls.Transfer.FULL)
    public ResponseEntity<OperationResult> transferMoney(
            @RequestBody @Valid MoneyTransferDto dto,
            @RequestHeader(CustomHeaders.USER_LOGIN) String login) {

        return ResponseEntity.ok(transferService.transferMoney(login, dto));
    }
}
