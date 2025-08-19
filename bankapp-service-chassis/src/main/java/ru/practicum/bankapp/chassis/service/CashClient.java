package ru.practicum.bankapp.chassis.service;

import ru.practicum.bankapp.lib.dto.cash.CashOperationDto;
import ru.practicum.bankapp.lib.dto.cash.OperationResult;

public interface CashClient {
    OperationResult executeCashOperation(String login, CashOperationDto cashOperationDto);
}
