package ru.practicum.bankapp.lib.dto.cash;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.bankapp.lib.common.constant.CashOperation;
import ru.practicum.bankapp.lib.common.constant.Currency;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class CashOperationDto {
    @NotNull
    private Currency currency;

    @NotNull
    private CashOperation action;

    @NotNull(message = "Сумма не может быть пустой")
    @Positive(message = "Сумма должна быть больше 0")
    private BigDecimal value;
}
