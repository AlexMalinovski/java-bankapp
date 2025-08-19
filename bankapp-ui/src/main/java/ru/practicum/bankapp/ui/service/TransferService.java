package ru.practicum.bankapp.ui.service;

import ru.practicum.bankapp.lib.dto.cash.OperationResult;
import ru.practicum.bankapp.lib.dto.transfer.MoneyTransferDto;

public interface TransferService {
    OperationResult transferMoney(String login, MoneyTransferDto transferDto);
}
