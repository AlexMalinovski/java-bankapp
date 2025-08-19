package ru.practicum.bankapp.notifications.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.bankapp.chassis.config.url.NotificationUrls;
import ru.practicum.bankapp.lib.common.constant.NotificationMethod;
import ru.practicum.bankapp.lib.dto.notification.NotificationDto;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NotificationControllerTest extends AbstractNotificationControllerTest {

    @Test
    @SneakyThrows
    void sendNotification() {
        NotificationDto notificationDto = NotificationDto.builder()
                .login("login")
                .method(NotificationMethod.LOG)
                .message("message")
                .critical(false)
                .build();

        mockMvc.perform(post(NotificationUrls.Notification.FULL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(notificationDto)))
                .andExpect(status().isOk());

        verify(notificationService).sendNotification(notificationDto);
    }
}