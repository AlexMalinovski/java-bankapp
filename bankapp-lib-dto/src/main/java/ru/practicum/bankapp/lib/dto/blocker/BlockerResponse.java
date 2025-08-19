package ru.practicum.bankapp.lib.dto.blocker;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Getter
@ToString
@Jacksonized
public class BlockerResponse {
    @JsonIgnore
    public static final BlockerResponse ERROR = BlockerResponse.builder()
            .accepted(false)
            .message("Невозможно проверить операцию")
            .build();

    private final Boolean accepted;
    private final String message;
}
