package ru.practicum.bankapp.lib.dto.accounts;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Getter
@ToString
@Jacksonized
public class UserProfileDto {
    private final String login;
    private final String password;
}
