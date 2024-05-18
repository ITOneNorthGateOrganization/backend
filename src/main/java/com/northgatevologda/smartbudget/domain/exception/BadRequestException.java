package com.northgatevologda.smartbudget.domain.exception;

/**
 * Custom exception class to represent a "Bad Request" exception.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
