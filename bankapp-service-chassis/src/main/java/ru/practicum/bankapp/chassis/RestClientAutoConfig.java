package ru.practicum.bankapp.chassis;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.client.OAuth2ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import ru.practicum.bankapp.chassis.config.ChassisPreferences;
import ru.practicum.bankapp.chassis.config.url.ServicesUrls;
import ru.practicum.bankapp.chassis.service.AccountsClient;
import ru.practicum.bankapp.chassis.service.BlockerClient;
import ru.practicum.bankapp.chassis.service.CashClient;
import ru.practicum.bankapp.chassis.service.ExchangeClient;
import ru.practicum.bankapp.chassis.service.ExchangeGenClient;
import ru.practicum.bankapp.chassis.service.NotificationsClient;
import ru.practicum.bankapp.chassis.service.TransferClient;
import ru.practicum.bankapp.chassis.service.impl.AccountsRestClient;
import ru.practicum.bankapp.chassis.service.impl.BlockerRestClient;
import ru.practicum.bankapp.chassis.service.impl.CashRestClient;
import ru.practicum.bankapp.chassis.service.impl.ExchangeGenRestClient;
import ru.practicum.bankapp.chassis.service.impl.ExchangeRestClient;
import ru.practicum.bankapp.chassis.service.impl.NotificationsRestClient;
import ru.practicum.bankapp.chassis.service.impl.TransferRestClient;

@AutoConfiguration
@EnableDiscoveryClient
@RequiredArgsConstructor
public class RestClientAutoConfig {

    @Bean
    @LoadBalanced
    RestClient.Builder serviceApiClient(OAuth2AuthorizedClientManager authorizedClientManager,
                                        ChassisPreferences chassisPreferences) {

        // (обновить)добавить oauth2-токен в заголовок запроса
        OAuth2ClientHttpRequestInterceptor requestInterceptor =
                new OAuth2ClientHttpRequestInterceptor(authorizedClientManager);
        requestInterceptor.setClientRegistrationIdResolver(request -> chassisPreferences.getOauthClientId());

        return RestClient.builder()
                .requestInterceptor(requestInterceptor);
    }

    @Bean
    AccountsClient accountsClient(RestClient.Builder serviceApiClient) {
        var restClient = serviceApiClient.baseUrl(ServicesUrls.Accounts.FULL);
        return new AccountsRestClient(restClient.build());
    }

    @Bean
    BlockerClient blockerClient(RestClient.Builder serviceApiClient) {
        var restClient = serviceApiClient.baseUrl(ServicesUrls.Blocker.FULL);
        return new BlockerRestClient(restClient.build());
    }

    @Bean
    CashClient cashClient(RestClient.Builder serviceApiClient) {
        var restClient = serviceApiClient.baseUrl(ServicesUrls.Cash.FULL);
        return new CashRestClient(restClient.build());
    }

    @Bean
    ExchangeClient exchangeClient(RestClient.Builder serviceApiClient) {
        var restClient = serviceApiClient.baseUrl(ServicesUrls.Exchange.FULL);
        return new ExchangeRestClient(restClient.build());
    }

    @Bean
    ExchangeGenClient exchangeGenClient(RestClient.Builder serviceApiClient) {
        var restClient = serviceApiClient.baseUrl(ServicesUrls.ExchangeGen.FULL);
        return new ExchangeGenRestClient(restClient.build());
    }

    @Bean
    NotificationsClient notificationsClient(RestClient.Builder serviceApiClient) {
        var restClient = serviceApiClient.baseUrl(ServicesUrls.Notification.FULL);
        return new NotificationsRestClient(restClient.build());
    }

    @Bean
    TransferClient transferClient(RestClient.Builder serviceApiClient) {
        var restClient = serviceApiClient.baseUrl(ServicesUrls.Transfer.FULL);
        return new TransferRestClient(restClient.build());
    }
}
