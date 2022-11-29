package com.virtualmarathon.core.exception.event;

public class WrongEventOrganizerException extends RuntimeException {

    public WrongEventOrganizerException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongEventOrganizerException(String message) {
        super(message);
    }

    public WrongEventOrganizerException(Throwable cause) {
        super(cause);
    }
}
