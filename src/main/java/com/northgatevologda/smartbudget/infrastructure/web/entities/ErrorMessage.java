package com.northgatevologda.smartbudget.infrastructure.web.entities;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * Class representing an error message.
 */
@Data
@Builder
public class ErrorMessage {
    private int status;
    private Date timestamp;
    private String message;
    private String details;
}
