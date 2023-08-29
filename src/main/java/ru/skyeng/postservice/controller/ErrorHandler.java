package ru.skyeng.postservice.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.skyeng.postservice.exceptions.UnknownDataException;
import ru.skyeng.postservice.exceptions.UnknownTypeDeliveryException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse incorrectParameter(final EntityNotFoundException e){
        log.info("404 {}", e.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND.toString(),
                e.getMessage());
    }

    @Value
    @Builder
    @AllArgsConstructor
    @Jacksonized
    public static class ErrorResponse {
        String status;
        String message;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime timestamp = LocalDateTime.now();
    }

}


