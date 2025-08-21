package ru.practicum.bankapp.notifications.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.bankapp.lib.common.constant.NotificationMethod;
import ru.practicum.bankapp.lib.dto.notification.NotificationDto;
import ru.practicum.bankapp.notifications.service.NotificationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendNotification(NotificationDto dto) {
        if (dto.getMethod() == NotificationMethod.LOG) {
            log.info("Отправлено уведомление для пользователя {}: {}", dto.getLogin(), dto.getMessage());
        }
    }
}
