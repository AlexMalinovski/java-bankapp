package ru.practicum.bankapp.lib.dto.accounts;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LoginDto {
    @NotNull
    @Pattern(regexp = "^[a-z]{3,32}$", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Должен состоять из 3..32 латинских букв")
    private String login;

    @NotNull
    private String password;
}
