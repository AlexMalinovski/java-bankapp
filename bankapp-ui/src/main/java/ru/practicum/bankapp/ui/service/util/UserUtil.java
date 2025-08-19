package ru.practicum.bankapp.ui.service.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@UtilityClass
public class UserUtil {
    public Optional<String> getCurrentUserLogin() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
