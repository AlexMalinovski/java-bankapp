package ru.practicum.bankapp.lib.dto.accounts;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import ru.practicum.bankapp.lib.common.marker.OnUiService;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Validated
@EqualsAndHashCode
public class UserAccountsDto {

    @NotBlank(groups = OnUiService.class)
    private String name;

    @NotNull(groups = OnUiService.class)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthdate;

    @NotNull
    private List<@Valid AccountDto> accounts;
}
