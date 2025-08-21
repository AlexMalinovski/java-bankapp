package ru.practicum.bankapp.ui.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.bankapp.chassis.service.TransferClient;
import ru.practicum.bankapp.lib.dto.cash.OperationResult;
import ru.practicum.bankapp.lib.dto.transfer.MoneyTransferDto;
import ru.practicum.bankapp.ui.service.TransferService;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {
    private final TransferClient transferClient;

    @Override
    public OperationResult transferMoney(String login, MoneyTransferDto transferDto) {
        try {
            return transferClient.transferMoney(login, transferDto);
        } catch (Exception ex) {
            log.error("Непридвиденная ошибка при выполнении перевода: {}", ex.getMessage(), ex);
            return new OperationResult(false, ex.getMessage());
        }
    }
}
