package ru.practicum.bankapp.accounts.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.bankapp.accounts.service.NotificationServiceAdapter;
import ru.practicum.bankapp.chassis.service.AbstractNotificationService;
import ru.practicum.bankapp.chassis.service.NotificationsClient;

@Component
@Slf4j
public class NotificationServiceAdapterImpl extends AbstractNotificationService
        implements NotificationServiceAdapter {

    public NotificationServiceAdapterImpl(@Autowired NotificationsClient notificationsClient) {
        super(notificationsClient);
    }
}
