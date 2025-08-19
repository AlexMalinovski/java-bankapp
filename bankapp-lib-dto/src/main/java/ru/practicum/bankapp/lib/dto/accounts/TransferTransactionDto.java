package ru.practicum.bankapp.lib.dto.accounts;

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
public class TransferTransactionDto {
    @NotNull
    private final String loginTo;

    @NotNull
    private final Currency currencyFrom;

    @NotNull
    private final Currency currencyTo;

    @NotNull
    private final BigDecimal valueFrom;

    @NotNull
    private final BigDecimal valueTo;
}
