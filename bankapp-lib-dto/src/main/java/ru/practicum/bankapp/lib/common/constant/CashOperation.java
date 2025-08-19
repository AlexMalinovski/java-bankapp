package ru.practicum.bankapp.lib.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CashOperation {
    PUT("Положить", 1), GET("Снять", -1);

    private final String title;
    private final long multiple;
}
