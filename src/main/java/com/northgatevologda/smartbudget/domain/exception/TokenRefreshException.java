package com.northgatevologda.smartbudget.domain.exception;

/**
 * Custom exception class for token refresh errors.
 */
public class TokenRefreshException extends RuntimeException {
    public TokenRefreshException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
