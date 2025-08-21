package ru.practicum.bankapp.chassis.service;

import ru.practicum.bankapp.lib.dto.cash.OperationResult;
import ru.practicum.bankapp.lib.dto.transfer.MoneyTransferDto;

public interface TransferClient {
    OperationResult transferMoney(String login, MoneyTransferDto transferDto);
}
