package ru.practicum.bankapp.notifications.service;

import ru.practicum.bankapp.lib.dto.notification.NotificationDto;

public interface NotificationService {
    void sendNotification(NotificationDto notificationDto);
}
