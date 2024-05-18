package com.northgatevologda.smartbudget.application.service.transaction.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Data class representing a transaction with details.
 */
@Data
@Builder
public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private Instant createdAt;
    private String description;
    private Long receiverId;
    private Long senderId;
    private Long transactionTypeId;
}
