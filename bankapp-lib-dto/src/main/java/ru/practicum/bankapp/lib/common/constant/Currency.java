package ru.practicum.bankapp.lib.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public enum Currency {
    RUB("Рубли"), USD("Доллары США"), CNY("Юани");

    private final String title;

    public static final List<String> ALL_NAMES = Arrays.stream(Currency.values())
                .map(Enum::name)
                .toList();

    public static final List<String> ALL_TITLES = Arrays.stream(Currency.values())
            .map(Currency::getTitle)
            .toList();

    public static final List<Currency> ALL = Arrays.stream(Currency.values())
            .toList();

    public static final Map<String, Currency> BY_NAMES = Arrays.stream(Currency.values())
            .collect(Collectors.toMap(Currency::name, Function.identity()));
}
