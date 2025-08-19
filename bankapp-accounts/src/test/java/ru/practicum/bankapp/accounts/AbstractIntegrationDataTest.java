package ru.practicum.bankapp.accounts;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.practicum.bankapp.accounts.repository.UserAccountRepository;
import ru.practicum.bankapp.accounts.repository.UserRepository;
import ru.practicum.bankapp.accounts.service.impl.AccountServiceImpl;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cloud.consul.port=8500", "spring.cloud.consul.host=localhost"})
public abstract class AbstractIntegrationDataTest {
    private static final PostgreSQLContainer<?> postgres;
    @Autowired
    protected PasswordEncoder dbUserPasswordEncoder;

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected UserAccountRepository userAccountRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected AccountServiceImpl accountService;

    static {
        postgres = new PostgreSQLContainer<>("postgres:15") // Имя и версия образа
                .withDatabaseName("testdb")
                .withUsername("junit")
                .withPassword("junit");
        postgres.start();
    }

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.driverClassName", postgres::getDriverClassName);
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
}
