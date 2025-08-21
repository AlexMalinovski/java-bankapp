package ru.practicum.bankapp.chassis.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@ToString
public class ChassisPreferences {
    private Integer servicePort;

    @Value("${spring.application.name}")
    private String serviceName;
    private String oauthClientId;
    private String oauthClientSecret;
    private String oauthIssuerUri;
}
