package ru.practicum.bankapp.lib.dto.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.bankapp.lib.common.marker.OnUiService;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class SignUpDto {
    @NotNull
    @Pattern(regexp = "^[a-z]{3,32}$", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Должен состоять из 3..32 латинских букв")
    private String login;

    @NotNull
    private String password;

    private String passwordConfirm;

    @NotBlank(groups = OnUiService.class)
    private String name;

    @NotNull(groups = OnUiService.class)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthdate;

    @JsonIgnore
    @AssertTrue(message = "Пароли не совпадают")
    public boolean isPasswordConfirmed() {
        return password == null || password.equals(passwordConfirm);
    }
}
