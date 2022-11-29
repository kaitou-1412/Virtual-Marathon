package com.virtualmarathon.core.exception.trackingDetails;

public class WrongParticipantException extends RuntimeException {

    public WrongParticipantException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongParticipantException(String message) {
        super(message);
    }

    public WrongParticipantException(Throwable cause) {
        super(cause);
    }
}
