package com.virtualmarathon.core.exception.trackingDetails;

public class TrackingDetailsNotFoundException extends RuntimeException {

    public TrackingDetailsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TrackingDetailsNotFoundException(String message) {
        super(message);
    }

    public TrackingDetailsNotFoundException(Throwable cause) {
        super(cause);
    }
}
