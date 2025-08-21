package ru.practicum.bankapp.transfer.service.impl;

import org.junit.jupiter.api.Test;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.dto.accounts.TransferTransactionDto;
import ru.practicum.bankapp.lib.dto.blocker.BlockerRequest;
import ru.practicum.bankapp.lib.dto.blocker.BlockerResponse;
import ru.practicum.bankapp.lib.dto.transfer.MoneyTransferDto;
import ru.practicum.bankapp.transfer.AbstractIntegrationTransferTest;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransferServiceImplTestIT extends AbstractIntegrationTransferTest {

    private static final String LOGIN_FROM = "loginFrom";
    private static final String LOGIN_TO = "loginTo";

    @Test
    void transferMoney() {
        MoneyTransferDto dto = MoneyTransferDto.builder()
                .currencyFrom(Currency.RUB).currencyTo(Currency.RUB)
                .loginTo(LOGIN_TO)
                .value(BigDecimal.TEN)
                .build();
        TransferTransactionDto transactionDto = TransferTransactionDto.builder()
                .currencyFrom(Currency.RUB).currencyTo(Currency.RUB)
                .loginTo(LOGIN_TO)
                .valueFrom(BigDecimal.TEN)
                .valueTo(BigDecimal.TEN)
                .build();
        BlockerRequest request = BlockerRequest.builder()
                .loginFrom(LOGIN_FROM)
                .loginTo(LOGIN_TO)
                .value(BigDecimal.TEN)
                .build();
        when(blockerClient.checkTransaction(request)).thenReturn(BlockerResponse.builder().accepted(true).build());

        var actual = transferService.transferMoney(LOGIN_FROM, dto);

        verify(accountsClient).runTransferTransaction(LOGIN_FROM, transactionDto);
    }
}