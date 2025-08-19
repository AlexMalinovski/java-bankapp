package ru.practicum.bankapp.lib.dto.exchange;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.bankapp.lib.common.constant.Currency;

import java.math.BigDecimal;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
@Jacksonized
public class ExchangeDto {
    @NotNull
    private final Currency currencyFrom;

    @NotNull
    private final Currency currencyTo;

    private final BigDecimal valueFrom;
    private final BigDecimal valueTo;

    @JsonIgnore
    @AssertTrue(message = "Только одна сумма должна быть заполнена")
    public boolean isValueNotNull() {
        return valueFrom == null || valueTo == null;
    }
}
