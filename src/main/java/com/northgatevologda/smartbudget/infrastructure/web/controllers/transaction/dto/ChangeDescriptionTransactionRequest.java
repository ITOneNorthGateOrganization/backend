package com.northgatevologda.smartbudget.infrastructure.web.controllers.transaction.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data class representing a request to change the description of a transaction.
 */
@Data
public class ChangeDescriptionTransactionRequest {
    @NotNull
    private String description;
}
