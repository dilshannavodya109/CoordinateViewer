package com.coordinate.viewer.model.exception;

public class CoordinateFetchException extends RuntimeException {
    public CoordinateFetchException(String message, Exception cause) {
        super(message, cause);
    }
}
