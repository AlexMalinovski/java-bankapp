package ru.practicum.bankapp.blocker.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.bankapp.blocker.service.CheckService;
import ru.practicum.bankapp.chassis.config.url.BlockerUrls;
import ru.practicum.bankapp.lib.dto.blocker.BlockerRequest;
import ru.practicum.bankapp.lib.dto.blocker.BlockerResponse;

@RestController
@RequiredArgsConstructor
public class CheckController {
    private final CheckService checkService;

    @PostMapping(BlockerUrls.Checker.FULL)
    ResponseEntity<BlockerResponse> checkOperation(@RequestBody @Valid BlockerRequest blockerRequest) {
        return ResponseEntity.ok(checkService.checkOperation(blockerRequest));
    }

}
