package com.virtualmarathon.core.exception.event;

public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventNotFoundException(String message) {
        super(message);
    }

    public EventNotFoundException(Throwable cause) {
        super(cause);
    }
}
