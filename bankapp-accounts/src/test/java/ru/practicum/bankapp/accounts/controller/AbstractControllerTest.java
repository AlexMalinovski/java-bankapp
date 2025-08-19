package ru.practicum.bankapp.accounts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.bankapp.accounts.config.SecurityTestConfig;
import ru.practicum.bankapp.accounts.service.AccountService;


@WebMvcTest(controllers = {UserController.class})
@WithMockUser(username = "client")
@Import(value = SecurityTestConfig.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cloud.consul.port=8500", "spring.cloud.consul.host=localhost"})
public abstract class AbstractControllerTest {

    @MockitoBean
    protected AccountService accountService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}
