package ru.practicum.bankapp.exchange.service.impl.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.bankapp.chassis.service.impl.mapper.DefaultMapperConfig;
import ru.practicum.bankapp.exchange.entity.Rate;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.dto.rate.RateDto;

import java.util.List;

@Mapper(config = DefaultMapperConfig.class)
public interface RateMapper {

    @Mapping(target = "currency", source = "dto.name")
    Rate toRate(RateDto dto);

    @Mapping(target = "name", source = "src.currency", qualifiedByName = "toCurrencyName")
    @Mapping(target = "title", source = "src.currency", qualifiedByName = "toCurrencyTitle")
    RateDto toRateDto(Rate src);

    List<RateDto> toRateDto(List<Rate> src);

    @Named("toCurrencyName")
    default String toCurrencyName(Currency currency) {
        if (currency == null) {
            return null;
        }
        return currency.name();
    }

    @Named("toCurrencyTitle")
    default String toCurrencyTitle(Currency currency) {
        if (currency == null) {
            return null;
        }
        return currency.getTitle();
    }
}
