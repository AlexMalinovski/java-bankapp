package ru.practicum.bankapp.blocker.service.impl;

import org.junit.jupiter.api.Test;
import ru.practicum.bankapp.lib.dto.blocker.BlockerRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CheckServiceBasicTest {

    private CheckServiceBasic checkService = new CheckServiceBasic();

    @Test
    void checkOperation() {
        BlockerRequest request = BlockerRequest.builder().build();

        var actual = checkService.checkOperation(request);

        assertNotNull(actual);
    }
}