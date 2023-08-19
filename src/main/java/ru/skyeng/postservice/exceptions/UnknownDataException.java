package ru.skyeng.postservice.exceptions;

import jakarta.persistence.PersistenceException;

public class UnknownDataException extends PersistenceException {
    public UnknownDataException(String message) {
        super(message);
    }
}
