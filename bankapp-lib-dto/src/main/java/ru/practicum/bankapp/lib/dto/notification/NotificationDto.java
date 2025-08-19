package ru.practicum.bankapp.lib.dto.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.bankapp.lib.common.constant.NotificationMethod;

@Builder(toBuilder = true)
@Getter
@ToString
@EqualsAndHashCode
@Jacksonized
public class NotificationDto {
    @NotBlank
    private final String login;

    @NotNull
    private final NotificationMethod method;

    @NotBlank
    private final String message;

    private final boolean critical;
}
