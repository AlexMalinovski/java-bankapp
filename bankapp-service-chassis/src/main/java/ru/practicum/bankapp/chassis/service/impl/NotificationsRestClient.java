package ru.practicum.bankapp.chassis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestClient;
import ru.practicum.bankapp.chassis.config.url.NotificationUrls;
import ru.practicum.bankapp.chassis.service.NotificationsClient;
import ru.practicum.bankapp.lib.dto.notification.NotificationDto;

@RequiredArgsConstructor
public class NotificationsRestClient implements NotificationsClient {
    private final RestClient restClient;

    @Override
    public void sendNotification(NotificationDto dto) {
        restClient.post()
                .uri(NotificationUrls.Notification.FULL)
                .body(dto)
                .retrieve().body(Void.class);
    }
}
