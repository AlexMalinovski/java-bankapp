package ru.practicum.bankapp.ui.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.bankapp.ui.config.SecurityUiTestConfig;
import ru.practicum.bankapp.ui.service.AccountServiceAdapter;
import ru.practicum.bankapp.ui.service.CashService;
import ru.practicum.bankapp.ui.service.ExchangeService;
import ru.practicum.bankapp.ui.service.TransferService;


@WebMvcTest(controllers = {LoginController.class, MainController.class, SignUpController.class, ApiController.class})
@WithMockUser(username = "login")
@Import(value = SecurityUiTestConfig.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cloud.consul.port=8500", "spring.cloud.consul.host=localhost"})
public abstract class AbstractUiControllerTest {

    @MockitoBean
    protected AccountServiceAdapter accountServiceAdapter;

    @MockitoBean
    protected CashService cashService;

    @MockitoBean
    protected TransferService transferService;

    @MockitoBean
    protected ExchangeService exchangeService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}
