package ru.practicum.bankapp.lib.dto.blocker;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
@Jacksonized
public class BlockerRequest {
    @NotBlank
    private final String loginFrom;

    @NotBlank
    private final String loginTo;

    @NotNull
    @PositiveOrZero
    private final BigDecimal value;
}
