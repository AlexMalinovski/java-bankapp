package ru.practicum.bankapp.cash.service.impl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.bankapp.chassis.service.impl.mapper.DefaultMapperConfig;
import ru.practicum.bankapp.lib.dto.blocker.BlockerRequest;
import ru.practicum.bankapp.lib.dto.cash.CashOperationDto;

@Mapper(config = DefaultMapperConfig.class)
public interface CashOperationMapper {

    @Mapping(target = "loginTo", source = "loginFrom")
    @Mapping(target = "loginFrom", source = "loginFrom")
    BlockerRequest toBlockerRequest(CashOperationDto dto, String loginFrom);
}
