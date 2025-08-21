package ru.practicum.bankapp.cash.service;

import ru.practicum.bankapp.lib.dto.cash.CashOperationDto;
import ru.practicum.bankapp.lib.dto.cash.OperationResult;

public interface CashService {
    OperationResult executeCashOperation(String login, CashOperationDto cashOperationDto);
}
