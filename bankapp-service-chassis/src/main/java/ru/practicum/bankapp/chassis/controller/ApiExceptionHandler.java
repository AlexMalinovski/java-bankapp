package ru.practicum.bankapp.chassis.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import ru.practicum.bankapp.chassis.dto.ProblemDetail;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleUnauthorized(HttpClientErrorException.Unauthorized ex, WebRequest request) {
        ProblemDetail problemDetail = new ProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ((ServletWebRequest) request).getRequest().getRequestURI());
        log.error(ex.getMessage());

        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleAllUncaught(Exception ex, WebRequest request) {
        ProblemDetail problemDetail = new ProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ((ServletWebRequest) request).getRequest().getRequestURI());
        log.error(ex.getMessage(), ex);

        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }
}
