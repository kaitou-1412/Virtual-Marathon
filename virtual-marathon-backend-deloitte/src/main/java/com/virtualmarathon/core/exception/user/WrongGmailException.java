package com.virtualmarathon.core.exception.user;

public class WrongGmailException extends RuntimeException{

    public WrongGmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongGmailException(String message) {
        super(message);
    }

    public WrongGmailException(Throwable cause) {
        super(cause);
    }
}
