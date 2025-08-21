package ru.practicum.bankapp.exchange.entity.id;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.bankapp.lib.common.constant.Currency;

import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class RateId implements Serializable {
    private LocalDateTime time;
    private Currency currency;
}
