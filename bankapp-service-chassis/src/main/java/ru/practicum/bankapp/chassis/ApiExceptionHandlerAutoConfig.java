package ru.practicum.bankapp.chassis;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.bankapp.chassis.controller.ApiExceptionHandler;

@AutoConfiguration
@ConditionalOnMissingBean(annotation = {ControllerAdvice.class, RestControllerAdvice.class})
public class ApiExceptionHandlerAutoConfig {
    @Bean
    public ApiExceptionHandler apiExceptionHandler() {
        return new ApiExceptionHandler();
    }
}
