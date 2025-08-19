package ru.practicum.bankapp.lib.dto.accounts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class PasswordChangeDto {
    @NotBlank
    private String passwordNew;

    @NotBlank
    private String passwordConfirm;

    @JsonIgnore
    @AssertTrue(message = "Пароли не совпадают")
    public boolean isPasswordEq() {
        return passwordNew == null || passwordConfirm == null || passwordNew.equals(passwordConfirm);
    }
}
