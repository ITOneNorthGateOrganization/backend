package com.northgatevologda.smartbudget.domain.exception;

/**
 * Custom exception class to represent a "Not Found" exception.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

