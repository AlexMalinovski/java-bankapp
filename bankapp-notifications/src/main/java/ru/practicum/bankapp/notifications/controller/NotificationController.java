package ru.practicum.bankapp.notifications.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.bankapp.chassis.config.url.NotificationUrls;
import ru.practicum.bankapp.lib.dto.notification.NotificationDto;
import ru.practicum.bankapp.notifications.service.NotificationService;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping(NotificationUrls.Notification.FULL)
    ResponseEntity<Void> sendNotification(@RequestBody @Valid NotificationDto notificationDto) {
        notificationService.sendNotification(notificationDto);
        return ResponseEntity.ok().build();
    }

}
