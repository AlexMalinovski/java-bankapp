package ru.practicum.bankapp.cash.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.bankapp.cash.service.CashService;
import ru.practicum.bankapp.cash.service.NotificationServiceAdapter;
import ru.practicum.bankapp.cash.service.impl.mapper.CashOperationMapper;
import ru.practicum.bankapp.chassis.service.AccountsClient;
import ru.practicum.bankapp.chassis.service.BlockerClient;
import ru.practicum.bankapp.lib.common.constant.NotificationMethod;
import ru.practicum.bankapp.lib.dto.blocker.BlockerResponse;
import ru.practicum.bankapp.lib.dto.cash.CashOperationDto;
import ru.practicum.bankapp.lib.dto.cash.OperationResult;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CashServiceImpl implements CashService {
    private final AccountsClient accountsClient;
    private final BlockerClient blockerClient;
    private final NotificationServiceAdapter notificationServiceAdapter;
    private final CashOperationMapper cashOperationMapper;

    @Override
    public OperationResult executeCashOperation(String login, CashOperationDto cashOperationDto) {
        log.info("Поступил запрос на операцию {} с наличными от пользователя {}",
                cashOperationDto.getAction(), login);
        try {
            var checkResult = Optional.of(cashOperationMapper.toBlockerRequest(cashOperationDto, login))
                    .map(blockerClient::checkTransaction)
                    .orElse(BlockerResponse.ERROR);

            if (Boolean.TRUE.equals(checkResult.getAccepted())) {
                accountsClient.runCashTransaction(login, cashOperationDto);
            } else {
                log.warn("Операции с наличными от пользователя {} признана подозрительной и заблокирована: {}",
                        login, checkResult.getMessage());
                return new OperationResult(false, checkResult.getMessage());
            }

        } catch (Exception ex) {
            log.info("Ошибка при выполнении операции с наличными от пользователя {}: {}", login, ex.getMessage());
            return new OperationResult(false, ex.getMessage());
        }

        log.info("Транзакция по операции с наличными пользователя {} выполнена", login);
        notificationServiceAdapter.sendNonCriticalNotification(login, NotificationMethod.LOG,
                String.format("Выполнена операция с наличными: %s %s %s",
                        cashOperationDto.getAction().getTitle(), cashOperationDto.getValue(),
                        cashOperationDto.getCurrency().name()));
        return new OperationResult(true, null);
    }
}
