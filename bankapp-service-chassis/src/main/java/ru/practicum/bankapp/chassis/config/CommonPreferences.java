package ru.practicum.bankapp.chassis.config;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class CommonPreferences {
    private String gatewayServiceName;
}
