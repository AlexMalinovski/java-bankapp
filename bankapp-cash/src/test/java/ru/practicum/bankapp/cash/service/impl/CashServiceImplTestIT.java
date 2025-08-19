package ru.practicum.bankapp.cash.service.impl;

import org.junit.jupiter.api.Test;
import ru.practicum.bankapp.cash.AbstractCashIntegrationTest;
import ru.practicum.bankapp.lib.common.constant.CashOperation;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.dto.blocker.BlockerRequest;
import ru.practicum.bankapp.lib.dto.blocker.BlockerResponse;
import ru.practicum.bankapp.lib.dto.cash.CashOperationDto;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class CashServiceImplTestIT extends AbstractCashIntegrationTest {

    @Test
    void executeCashOperation() {
        CashOperationDto cashOperationDto = new CashOperationDto(Currency.RUB, CashOperation.PUT, BigDecimal.TEN);
        when(blockerClient.checkTransaction(BlockerRequest.builder()
                .loginFrom("login")
                .loginTo("login")
                .value(cashOperationDto.getValue())
                .build()))
                .thenReturn(BlockerResponse.builder().accepted(true).build());

        var actual = cashService.executeCashOperation("login", cashOperationDto);


        verify(accountsClient).runCashTransaction("login", cashOperationDto);
    }
}