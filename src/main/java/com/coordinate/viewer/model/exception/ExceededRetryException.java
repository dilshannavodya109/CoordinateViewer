package com.coordinate.viewer.model.exception;

public class ExceededRetryException extends RuntimeException {
    public ExceededRetryException(String message, RuntimeException cause) {
        super(message, cause);
    }
}
