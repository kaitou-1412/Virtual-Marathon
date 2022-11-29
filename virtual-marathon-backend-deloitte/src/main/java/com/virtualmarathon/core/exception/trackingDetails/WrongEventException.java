package com.virtualmarathon.core.exception.trackingDetails;

public class WrongEventException extends RuntimeException {

    public WrongEventException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongEventException(String message) {
        super(message);
    }

    public WrongEventException(Throwable cause) {
        super(cause);
    }
}
