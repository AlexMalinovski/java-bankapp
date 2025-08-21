package ru.practicum.bankapp.transfer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.practicum.bankapp.chassis.service.AccountsClient;
import ru.practicum.bankapp.chassis.service.BlockerClient;
import ru.practicum.bankapp.chassis.service.ExchangeClient;
import ru.practicum.bankapp.chassis.service.NotificationsClient;
import ru.practicum.bankapp.transfer.service.impl.TransferServiceImpl;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cloud.consul.port=8500", "spring.cloud.consul.host=localhost"})
public abstract class AbstractIntegrationTransferTest {
    @MockitoBean
    protected AccountsClient accountsClient;

    @MockitoBean
    protected ExchangeClient exchangeClient;

    @MockitoBean
    protected BlockerClient blockerClient;

    @MockitoBean
    protected NotificationsClient notificationsClient;

    @Autowired
    protected TransferServiceImpl transferService;
}
