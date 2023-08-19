package ru.skyeng.postservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.skyeng.postservice.exceptions.UnknownTypeDeliveryException;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(UnknownTypeDeliveryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse incorrectParameter(final UnknownTypeDeliveryException e){
        return new ErrorResponse("Error", String.format("Ошибка “%s”", e.getMessage()));
    }

}

class ErrorResponse {
    private final String error;
    private final String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }
}
