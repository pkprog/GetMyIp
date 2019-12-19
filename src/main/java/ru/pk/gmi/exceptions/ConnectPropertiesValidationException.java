package ru.pk.gmi.exceptions;

public class ConnectPropertiesValidationException extends RuntimeException {
    public ConnectPropertiesValidationException() {
    }

    public ConnectPropertiesValidationException(String message) {
        super(message);
    }
}
