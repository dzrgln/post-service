package ru.skyeng.postservice.exceptions;

public class UnknownTypeDeliveryException extends Exception{
    public UnknownTypeDeliveryException(String message) {
        super(message);
    }
}
