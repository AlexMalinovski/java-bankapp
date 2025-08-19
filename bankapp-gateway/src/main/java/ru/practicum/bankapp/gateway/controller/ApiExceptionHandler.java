package ru.practicum.bankapp.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import reactor.core.publisher.Mono;
import ru.practicum.bankapp.gateway.dto.ProblemDetail;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler
    public Mono<ResponseEntity<Object>> handleUnauthorized(HttpClientErrorException.Unauthorized ex, WebRequest request) {
        ProblemDetail problemDetail = new ProblemDetail(
                HttpStatus.UNAUTHORIZED,
                null);
        log.error(ex.getMessage());

        return Mono.just(ResponseEntity.status(problemDetail.getStatus()).body(problemDetail));
    }

    @ExceptionHandler
    public Mono<ResponseEntity<Object>> handleAllUncaught(Exception ex) {
        ProblemDetail problemDetail = new ProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                null);
        log.error(ex.getMessage());

        return Mono.just(ResponseEntity.status(problemDetail.getStatus()).body(problemDetail));
    }

}
