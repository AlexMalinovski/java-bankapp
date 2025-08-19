package ru.practicum.bankapp.chassis;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import ru.practicum.bankapp.chassis.config.ChassisPreferences;
import ru.practicum.bankapp.chassis.config.CommonPreferences;

@AutoConfiguration
public class PreferencesAutoConfig {

    @Bean
    @ConfigurationProperties(prefix = "chassis")
    ChassisPreferences chassisPreferences() {
        var pref = new ChassisPreferences();
        return pref;
    }

    @Bean
    CommonPreferences commonPreferences() {
        return CommonPreferences.builder()
                .gatewayServiceName("gateway")
                .build();
    }
}
