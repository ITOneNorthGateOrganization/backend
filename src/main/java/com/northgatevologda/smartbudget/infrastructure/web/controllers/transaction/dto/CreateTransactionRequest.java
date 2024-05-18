package com.northgatevologda.smartbudget.infrastructure.web.controllers.transaction.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data class representing a request to save a transaction.
 */
@Data
public class CreateTransactionRequest {
    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal amount;
    @NotNull
    private String description;
    @NotNull
    private Long transactionTypeId;
    private Long receiverId;
    private Long senderId;
}
