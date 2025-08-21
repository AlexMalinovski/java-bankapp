package ru.practicum.bankapp.lib.dto.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.bankapp.lib.common.constant.Currency;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class AccountDto {
    @NotNull
    private Long id;

    @NotNull
    private String userLogin;

    @NotNull
    private Currency currency;

    @NotNull
    private BigDecimal value;

    @NotNull
    private Boolean exists;

    @JsonIgnore
    @AssertTrue(message = "Только счета с нулевым остатком могут быть деактивированы")
    public boolean isNotExistMustZeroValue() {
        return exists == null || Boolean.TRUE.equals(exists) || value == null
                || BigDecimal.ZERO.compareTo(value) == 0;
    }
}
