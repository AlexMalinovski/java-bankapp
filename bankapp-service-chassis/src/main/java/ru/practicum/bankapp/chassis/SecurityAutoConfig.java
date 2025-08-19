package ru.practicum.bankapp.chassis;

import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.practicum.bankapp.chassis.config.ChassisPreferences;
import ru.practicum.bankapp.chassis.controller.JwtFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@AutoConfiguration
@EnableWebSecurity
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
public class SecurityAutoConfig {
    @Bean
    @ConditionalOnMissingBean(name = "securityFilterChain")
    public SecurityFilterChain securityFilterChain(HttpSecurity http, ChassisPreferences chassisPreferences) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(custom -> custom.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/actuator/**").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(customizer -> customizer
                        .jwt(jwtCustomizer -> {
                            String issuerUri = chassisPreferences.getOauthIssuerUri();
                            jwtCustomizer.jwkSetUri(issuerUri + "/protocol/openid-connect/certs");
                            // Создаём стандартный конвертер...
                            JwtAuthenticationConverter authConverter = new JwtAuthenticationConverter();
                            // ...но переопределяем алгоритм извлечения привилегий пользователя.
                            authConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
                                List<String> roles = Optional.ofNullable(jwt.getClaimAsStringList("roles"))
                                        .orElse(List.of());

                                return roles.stream()
                                        .map(SimpleGrantedAuthority::new)
                                        .collect(Collectors.toList());
                            });
                            // Указываем настроенный конвертер аутентификаций
                            jwtCustomizer.jwtAuthenticationConverter(authConverter);
                        }))
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    Filter jwtTokenFilter() {
        return new JwtFilter();
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService
    ) {
        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);

        manager.setAuthorizedClientProvider(OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .refreshToken()
                .build());

        return manager;
    }

    @Bean
    @ConditionalOnMissingBean(ClientRegistrationRepository.class)
    InMemoryClientRegistrationRepository clientRegistrationRepository(ChassisPreferences chassisPreferences) {
        List<ClientRegistration> registrations = new ArrayList<>();
        registrations.add(internalClientRegistration(chassisPreferences));
        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration internalClientRegistration(ChassisPreferences chassisPreferences) {
        return ClientRegistration.withRegistrationId("bankapp-srv")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .clientId(chassisPreferences.getOauthClientId())
                .clientSecret(chassisPreferences.getOauthClientSecret())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .scope("profile")
                .issuerUri(chassisPreferences.getOauthIssuerUri())
                .tokenUri(chassisPreferences.getOauthIssuerUri() + "/protocol/openid-connect/token")
                .clientName("Keycloak")
                .build();
    }
}

