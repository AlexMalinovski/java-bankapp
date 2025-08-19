package ru.practicum.bankapp.blocker.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.bankapp.chassis.config.url.BlockerUrls;
import ru.practicum.bankapp.chassis.config.url.CustomHeaders;
import ru.practicum.bankapp.lib.dto.blocker.BlockerRequest;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CheckControllerTestIT extends AbstractBlockerControllerTest {

    @Test
    @SneakyThrows
    void checkOperation() {
        BlockerRequest blockerRequest = BlockerRequest.builder()
                .loginFrom("from").loginTo("to")
                .value(BigDecimal.TEN)
                .build();

        mockMvc.perform(post(BlockerUrls.Checker.FULL)
                        .header(CustomHeaders.USER_LOGIN, "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(blockerRequest)))
                .andExpect(status().isOk());


        verify(checkService).checkOperation(blockerRequest);
    }
}