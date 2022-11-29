package com.virtualmarathon.core.exception.event;

public class WrongEventStatusException extends RuntimeException {

    public WrongEventStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongEventStatusException(String message) {
        super(message);
    }

    public WrongEventStatusException(Throwable cause) {
        super(cause);
    }
}
