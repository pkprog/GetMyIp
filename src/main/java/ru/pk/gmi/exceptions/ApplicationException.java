package ru.pk.gmi.exceptions;

public class ApplicationException extends RuntimeException {
    public ApplicationException() {
    }

    public ApplicationException(String message) {
        super(message);
        this.printStackTrace();
    }

    public ApplicationException(String message, Throwable parent) {
        super(message, parent);
        this.printStackTrace();
    }
}
