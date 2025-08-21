package ru.practicum.bankapp.transfer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.bankapp.chassis.service.AccountsClient;
import ru.practicum.bankapp.chassis.service.BlockerClient;
import ru.practicum.bankapp.chassis.service.ExchangeClient;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.dto.blocker.BlockerResponse;
import ru.practicum.bankapp.lib.dto.cash.OperationResult;
import ru.practicum.bankapp.lib.dto.exchange.ExchangeDto;
import ru.practicum.bankapp.lib.dto.rate.RateDto;
import ru.practicum.bankapp.lib.dto.transfer.MoneyTransferDto;
import ru.practicum.bankapp.transfer.service.NotificationServiceAdapter;
import ru.practicum.bankapp.transfer.service.TransferService;
import ru.practicum.bankapp.transfer.service.impl.mapper.TransferMapper;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {
    private static final RateDto RUB_RATE = new RateDto(Currency.RUB.getTitle(), Currency.RUB.name(), BigDecimal.ONE, null);

    private final AccountsClient accountsClient;
    private final ExchangeClient exchangeClient;
    private final BlockerClient blockerClient;
    private final TransferMapper transferMapper;
    private final NotificationServiceAdapter notificationServiceAdapter;

    @Override
    public OperationResult transferMoney(String login, MoneyTransferDto dto) {
        log.info("Поступил запрос на перевод {}->{} {}: {}",
                login, dto.getLoginTo(), dto.getCurrencyTo(), dto.getValue());

        final BigDecimal valueTo;
        try {
            if (dto.getCurrencyFrom() != dto.getCurrencyTo()) {
                ;
                ExchangeDto exchangeRequest = transferMapper.toExchangeDto(dto);
                ExchangeDto exchangeResult = exchangeClient.exchange(exchangeRequest);
                valueTo = exchangeResult.getValueTo();
            } else {
                valueTo = dto.getValue();
            }
            var transactionDto = transferMapper.toTransferTransactionDto(dto, valueTo);

            BlockerResponse checkResult = Optional.of(transferMapper.toBlockerRequest(dto, login))
                    .map(blockerClient::checkTransaction)
                    .orElse(BlockerResponse.ERROR);

            if (Boolean.TRUE.equals(checkResult.getAccepted())) {
                accountsClient.runTransferTransaction(login, transactionDto);
            } else {
                log.warn("Перевод {}->{} признан подозрительным и заблокирован: {}",
                        login, transactionDto.getLoginTo(), checkResult.getMessage());
                return new OperationResult(false, checkResult.getMessage());
            }

        } catch (Exception ex) {
            log.info("Ошибка при переводе со счета пользователя {}: {}", login, ex.getMessage());
            return new OperationResult(false, ex.getMessage());
        }
        log.info("Транзакция по переводу со счета пользователя {} выполнена", login);
        notificationServiceAdapter.sendTransferNotifications(login, dto, valueTo);
        return new OperationResult(true, null);
    }
}
