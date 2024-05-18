package com.northgatevologda.smartbudget.infrastructure.web;

import com.northgatevologda.smartbudget.domain.exception.BadRequestException;
import com.northgatevologda.smartbudget.domain.exception.ForbiddenException;
import com.northgatevologda.smartbudget.domain.exception.NotFoundException;
import com.northgatevologda.smartbudget.domain.exception.TokenRefreshException;
import com.northgatevologda.smartbudget.infrastructure.web.entities.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

/**
 * Controller advice class for handling exceptions related to token refresh.
 */
@RestControllerAdvice
public class GlobalAdviceController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception ex) {
        if (ex.getCause() instanceof NotFoundException notfoundexception) {
            return handleNotFoundException(notfoundexception);
        } else {
            return buildErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        }
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<ErrorMessage> handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .timestamp(new Date())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorMessage> handleForbiddenException(ForbiddenException ex) {
        return buildErrorMessage(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundException(NotFoundException ex) {
        return buildErrorMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> handleBadRequestException(BadRequestException ex) {
        return buildErrorMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    private ResponseEntity<ErrorMessage> buildErrorMessage(int status, String details) {
        ErrorMessage errorMessage = ErrorMessage.builder()
                .status(status)
                .timestamp(new Date())
                .message("An error occurred")
                .details(details)
                .build();
        return new ResponseEntity<>(errorMessage, HttpStatusCode.valueOf(status));
    }
}