package ru.practicum.bankapp.chassis;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import ru.practicum.bankapp.chassis.config.ChassisPreferences;

@AutoConfiguration
public class AppAutoConfig {

    @Bean
    WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer(
            ChassisPreferences chassisPreferences) {
        return factory -> {
            factory.setPort(chassisPreferences.getServicePort());
        };
    }
}
