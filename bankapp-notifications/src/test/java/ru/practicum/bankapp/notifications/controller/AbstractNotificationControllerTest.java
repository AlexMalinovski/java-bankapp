package ru.practicum.bankapp.notifications.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.bankapp.notifications.config.NotificationSecurityTestConfig;
import ru.practicum.bankapp.notifications.service.NotificationService;


@WebMvcTest(controllers = {NotificationController.class})
@WithMockUser(username = "client")
@Import(value = NotificationSecurityTestConfig.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cloud.consul.port=8500", "spring.cloud.consul.host=localhost"})
public abstract class AbstractNotificationControllerTest {

    @MockitoBean
    protected NotificationService notificationService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}
