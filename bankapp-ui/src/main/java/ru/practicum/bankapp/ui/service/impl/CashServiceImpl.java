package ru.practicum.bankapp.ui.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.bankapp.chassis.service.CashClient;
import ru.practicum.bankapp.lib.dto.cash.CashOperationDto;
import ru.practicum.bankapp.lib.dto.cash.OperationResult;
import ru.practicum.bankapp.ui.service.CashService;

@Component
@RequiredArgsConstructor
@Slf4j
public class CashServiceImpl implements CashService {
    private final CashClient cashClient;

    @Override
    public OperationResult executeCashOperation(String login, CashOperationDto cashOperationDto) {
        try {
            return cashClient.executeCashOperation(login, cashOperationDto);
        } catch (Exception ex) {
            log.error("Непридвиденная ошибка при выполнении операции с наличными: {}", ex.getMessage(), ex);
            return new OperationResult(false, ex.getMessage());
        }
    }
}
