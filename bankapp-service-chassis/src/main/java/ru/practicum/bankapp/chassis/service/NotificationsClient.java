package ru.practicum.bankapp.chassis.service;

import ru.practicum.bankapp.lib.dto.notification.NotificationDto;

public interface NotificationsClient {
    void sendNotification(NotificationDto dto);
}
