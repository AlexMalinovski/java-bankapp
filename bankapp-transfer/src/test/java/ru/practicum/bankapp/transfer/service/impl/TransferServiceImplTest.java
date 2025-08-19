package ru.practicum.bankapp.transfer.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.bankapp.chassis.service.AccountsClient;
import ru.practicum.bankapp.chassis.service.BlockerClient;
import ru.practicum.bankapp.chassis.service.ExchangeClient;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.dto.accounts.TransferTransactionDto;
import ru.practicum.bankapp.lib.dto.blocker.BlockerRequest;
import ru.practicum.bankapp.lib.dto.blocker.BlockerResponse;
import ru.practicum.bankapp.lib.dto.exchange.ExchangeDto;
import ru.practicum.bankapp.lib.dto.transfer.MoneyTransferDto;
import ru.practicum.bankapp.transfer.service.NotificationServiceAdapter;
import ru.practicum.bankapp.transfer.service.impl.mapper.TransferMapper;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceImplTest {

    @Mock
    private AccountsClient accountsClient;

    @Mock
    private ExchangeClient exchangeClient;

    @Mock
    private BlockerClient blockerClient;

    @Mock
    private TransferMapper transferMapper;

    @Mock
    private NotificationServiceAdapter notificationServiceAdapter;

    @InjectMocks
    private TransferServiceImpl transferService;


    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void transferMoney_whenOneCurrency(boolean checkResult) {
        MoneyTransferDto dto = MoneyTransferDto.builder()
                .currencyFrom(Currency.RUB).currencyTo(Currency.RUB)
                .loginTo("loginTo")
                .value(BigDecimal.TEN)
                .build();
        BlockerResponse blockerResponse = BlockerResponse.builder()
                .accepted(checkResult).message("check")
                .build();


        TransferTransactionDto transactionDto = TransferTransactionDto.builder().build();
        BlockerRequest blockerRequest = BlockerRequest.builder().build();

        when(transferMapper.toTransferTransactionDto(dto, dto.getValue())).thenReturn(transactionDto);
        when(transferMapper.toBlockerRequest(dto, "loginFrom")).thenReturn(blockerRequest);
        when(blockerClient.checkTransaction(blockerRequest)).thenReturn(blockerResponse);

        var actual = transferService.transferMoney("loginFrom", dto);

        assertEquals(checkResult, actual.success());
        if (!checkResult) {
            assertEquals(blockerResponse.getMessage(), actual.error());
        } else {
            verify(accountsClient).runTransferTransaction("loginFrom", transactionDto);
            verify(notificationServiceAdapter).sendTransferNotifications(
                    "loginFrom", dto, dto.getValue());
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void transferMoney_whenDifferentCurrency(boolean checkResult) {
        MoneyTransferDto dto = MoneyTransferDto.builder()
                .currencyFrom(Currency.RUB).currencyTo(Currency.USD)
                .loginTo("loginTo")
                .value(BigDecimal.TEN)
                .build();
        BlockerResponse blockerResponse = BlockerResponse.builder()
                .accepted(checkResult).message("check")
                .build();


        TransferTransactionDto transactionDto = TransferTransactionDto.builder().build();
        BlockerRequest blockerRequest = BlockerRequest.builder().build();

        ExchangeDto exchangeRequest = ExchangeDto.builder().build();
        ExchangeDto exchangeResponse = ExchangeDto.builder().valueTo(BigDecimal.ONE).build();
        when(transferMapper.toExchangeDto(dto)).thenReturn(exchangeRequest);
        when(exchangeClient.exchange(exchangeRequest)).thenReturn(exchangeResponse);

        when(transferMapper.toTransferTransactionDto(dto, exchangeResponse.getValueTo())).thenReturn(transactionDto);
        when(transferMapper.toBlockerRequest(dto, "loginFrom")).thenReturn(blockerRequest);
        when(blockerClient.checkTransaction(blockerRequest)).thenReturn(blockerResponse);

        var actual = transferService.transferMoney("loginFrom", dto);

        assertEquals(checkResult, actual.success());
        if (!checkResult) {
            assertEquals(blockerResponse.getMessage(), actual.error());
        } else {
            verify(accountsClient).runTransferTransaction("loginFrom", transactionDto);
            verify(notificationServiceAdapter).sendTransferNotifications(
                    "loginFrom", dto, exchangeResponse.getValueTo());
        }
    }

    @Test
    void transferMoney_whenTransferTransactionFail_thenNotSuccess() {
        MoneyTransferDto dto = MoneyTransferDto.builder()
                .currencyFrom(Currency.RUB).currencyTo(Currency.RUB)
                .loginTo("loginTo")
                .value(BigDecimal.TEN)
                .build();
        BlockerResponse blockerResponse = BlockerResponse.builder()
                .accepted(true).message("check")
                .build();

        TransferTransactionDto transactionDto = TransferTransactionDto.builder().build();
        BlockerRequest blockerRequest = BlockerRequest.builder().build();

        when(transferMapper.toTransferTransactionDto(dto, dto.getValue())).thenReturn(transactionDto);
        when(transferMapper.toBlockerRequest(dto, "loginFrom")).thenReturn(blockerRequest);
        when(blockerClient.checkTransaction(blockerRequest)).thenReturn(blockerResponse);

        doThrow(new RuntimeException("Bad transaction")).when(accountsClient)
                .runTransferTransaction(any(), any());

        var actual = transferService.transferMoney("loginFrom", dto);

        assertEquals(false, actual.success());
        assertEquals("Bad transaction", actual.error());
    }

    @Test
    void transferMoney_whenBlockerCheckException_thenNotSuccess() {
        MoneyTransferDto dto = MoneyTransferDto.builder()
                .currencyFrom(Currency.RUB).currencyTo(Currency.RUB)
                .loginTo("loginTo")
                .value(BigDecimal.TEN)
                .build();
        BlockerResponse blockerResponse = BlockerResponse.builder()
                .accepted(true).message("check")
                .build();

        TransferTransactionDto transactionDto = TransferTransactionDto.builder().build();
        BlockerRequest blockerRequest = BlockerRequest.builder().build();

        when(transferMapper.toTransferTransactionDto(dto, dto.getValue())).thenReturn(transactionDto);
        when(transferMapper.toBlockerRequest(dto, "loginFrom")).thenReturn(blockerRequest);
        when(blockerClient.checkTransaction(blockerRequest)).thenThrow(new RuntimeException("Check exc"));


        var actual = transferService.transferMoney("loginFrom", dto);

        assertEquals(false, actual.success());
        assertEquals("Check exc", actual.error());
    }
}