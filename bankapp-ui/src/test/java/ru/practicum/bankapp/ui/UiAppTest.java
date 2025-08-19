package ru.practicum.bankapp.ui;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {"spring.cloud.consul.port=8500", "spring.cloud.consul.host=localhost"})
class UiAppTest {

    @Test
    void main() {
    }
}