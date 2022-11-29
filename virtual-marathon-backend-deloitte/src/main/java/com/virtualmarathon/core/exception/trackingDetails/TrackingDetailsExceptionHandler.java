package com.virtualmarathon.core.exception.trackingDetails;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TrackingDetailsExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<TrackingDetailsErrorResponse> handleException(TrackingDetailsNotFoundException exc) {
        TrackingDetailsErrorResponse error = new TrackingDetailsErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<TrackingDetailsErrorResponse> handleException(WrongParticipantException exc) {
        TrackingDetailsErrorResponse error = new TrackingDetailsErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<TrackingDetailsErrorResponse> handleException(WrongEventException exc) {
        TrackingDetailsErrorResponse error = new TrackingDetailsErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<TrackingDetailsErrorResponse> handleException(Exception exc) {
        TrackingDetailsErrorResponse error = new TrackingDetailsErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
