package ru.practicum.bankapp.lib.dto.rate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RateDto {
    private String title;

    @NotBlank
    private String name;

    @NotNull
    private BigDecimal value;

    @NotNull
    private LocalDateTime time;
}
