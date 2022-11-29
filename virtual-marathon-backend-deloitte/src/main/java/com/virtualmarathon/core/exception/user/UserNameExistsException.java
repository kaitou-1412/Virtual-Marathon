package com.virtualmarathon.core.exception.user;

public class UserNameExistsException extends RuntimeException {

    public UserNameExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNameExistsException(String message) {
        super(message);
    }

    public UserNameExistsException(Throwable cause) {
        super(cause);
    }
}
