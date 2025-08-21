package ru.practicum.bankapp.transfer.service.impl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.bankapp.chassis.service.impl.mapper.DefaultMapperConfig;
import ru.practicum.bankapp.lib.dto.accounts.TransferTransactionDto;
import ru.practicum.bankapp.lib.dto.blocker.BlockerRequest;
import ru.practicum.bankapp.lib.dto.exchange.ExchangeDto;
import ru.practicum.bankapp.lib.dto.transfer.MoneyTransferDto;

import java.math.BigDecimal;

@Mapper(config = DefaultMapperConfig.class)
public interface TransferMapper {

    @Mapping(target = "valueFrom", source = "dto.value")
    @Mapping(target = "valueTo", source = "valueTo")
    TransferTransactionDto toTransferTransactionDto(MoneyTransferDto dto, BigDecimal valueTo);

    @Mapping(target = "valueFrom", source = "value")
    ExchangeDto toExchangeDto(MoneyTransferDto dto);

    BlockerRequest toBlockerRequest(MoneyTransferDto dto, String loginFrom);

}
