package ru.practicum.bankapp.chassis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.TextUtils;
import org.springframework.util.Assert;
import ru.practicum.bankapp.lib.common.constant.NotificationMethod;
import ru.practicum.bankapp.lib.dto.notification.NotificationDto;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractNotificationService {
    private final NotificationsClient notificationsClient;

    public void sendNonCriticalNotification(String receiver, NotificationMethod method, String message) {
        Assert.isTrue(!TextUtils.isBlank(receiver) && !TextUtils.isBlank(message) && method != null,
                "Аргументы не могут быть пустыми");
        NotificationDto dto = NotificationDto.builder()
                .login(receiver)
                .message(message)
                .method(method)
                .critical(false)
                .build();
        try {
            notificationsClient.sendNotification(dto);
        } catch (Exception ex) {
            log.warn("Сбой отправки уведомления: {}", dto);
        }
    }
}
