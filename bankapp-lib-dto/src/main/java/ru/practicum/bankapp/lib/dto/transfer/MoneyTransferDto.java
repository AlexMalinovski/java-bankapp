package ru.practicum.bankapp.lib.dto.transfer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.bankapp.lib.common.constant.Currency;

import java.math.BigDecimal;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@Jacksonized
public class MoneyTransferDto {

    @NotNull
    private Currency currencyFrom;

    @NotNull
    private Currency currencyTo;

    private String loginTo;

    @NotNull
    @Positive
    private BigDecimal value;
}
