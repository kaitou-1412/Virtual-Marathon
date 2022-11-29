package com.virtualmarathon.core.exception.event;

public class WrongEventTypeException extends RuntimeException {

    public WrongEventTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongEventTypeException(String message) {
        super(message);
    }

    public WrongEventTypeException(Throwable cause) {
        super(cause);
    }
}
