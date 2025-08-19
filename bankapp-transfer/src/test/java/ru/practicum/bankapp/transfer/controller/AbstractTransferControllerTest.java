package ru.practicum.bankapp.transfer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.bankapp.transfer.config.TransferSecurityTestConfig;
import ru.practicum.bankapp.transfer.service.TransferService;


@WebMvcTest(controllers = {TransferController.class})
@WithMockUser(username = "client")
@Import(value = TransferSecurityTestConfig.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cloud.consul.port=8500", "spring.cloud.consul.host=localhost"})
public abstract class AbstractTransferControllerTest {

    @MockitoBean
    protected TransferService transferService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}
