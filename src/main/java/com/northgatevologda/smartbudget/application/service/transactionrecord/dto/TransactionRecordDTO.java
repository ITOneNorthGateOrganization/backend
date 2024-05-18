package com.northgatevologda.smartbudget.application.service.transactionrecord.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Data class representing a transaction record with details.
 */
@Data
@Builder
public class TransactionRecordDTO {
    private Long id;
    private BigDecimal amount;
    private Long budgetId;
    private Long categoryId;
    private String description;
}
