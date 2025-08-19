package ru.practicum.bankapp.cash.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.bankapp.cash.service.NotificationServiceAdapter;
import ru.practicum.bankapp.cash.service.impl.mapper.CashOperationMapper;
import ru.practicum.bankapp.chassis.service.AccountsClient;
import ru.practicum.bankapp.chassis.service.BlockerClient;
import ru.practicum.bankapp.lib.common.constant.CashOperation;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.common.constant.NotificationMethod;
import ru.practicum.bankapp.lib.dto.blocker.BlockerRequest;
import ru.practicum.bankapp.lib.dto.blocker.BlockerResponse;
import ru.practicum.bankapp.lib.dto.cash.CashOperationDto;
import ru.practicum.bankapp.lib.dto.cash.OperationResult;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CashServiceImplTest {
    private static final String LOGIN = "login";
    @Mock
    private AccountsClient accountsClient;

    @Mock
    private BlockerClient blockerClient;

    @Mock
    private NotificationServiceAdapter notificationServiceAdapter;

    @Mock
    private CashOperationMapper cashOperationMapper;

    @InjectMocks
    private CashServiceImpl cashService;

    @Test
    void executeCashOperation_whenBlocked_thenNotSuccess() {
        CashOperationDto cashOperationDto = new CashOperationDto(Currency.RUB, CashOperation.PUT, BigDecimal.TEN);
        BlockerRequest blockerRequest = BlockerRequest.builder().build();
        when(cashOperationMapper.toBlockerRequest(cashOperationDto, LOGIN)).thenReturn(blockerRequest);
        when(blockerClient.checkTransaction(blockerRequest))
                .thenReturn(BlockerResponse.builder().accepted(false).message("blocked").build());

        OperationResult actual = cashService.executeCashOperation(LOGIN, cashOperationDto);

        assertFalse(actual.success());
        assertEquals("blocked", actual.error());
    }

    @Test
    void executeCashOperation() {
        CashOperationDto cashOperationDto = new CashOperationDto(Currency.RUB, CashOperation.PUT, BigDecimal.TEN);
        BlockerRequest blockerRequest = BlockerRequest.builder().build();
        when(cashOperationMapper.toBlockerRequest(cashOperationDto, LOGIN)).thenReturn(blockerRequest);
        when(blockerClient.checkTransaction(blockerRequest))
                .thenReturn(BlockerResponse.builder().accepted(true).build());

        OperationResult actual = cashService.executeCashOperation(LOGIN, cashOperationDto);

        assertTrue(actual.success());
        verify(accountsClient).runCashTransaction(LOGIN, cashOperationDto);
        verify(notificationServiceAdapter).sendNonCriticalNotification(
                LOGIN, NotificationMethod.LOG, "Выполнена операция с наличными: Положить 10 RUB");
    }
}