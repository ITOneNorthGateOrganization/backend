package com.northgatevologda.smartbudget.application.service.transactiontype.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionTypeDTO {
    private Long id;
    private String name;
}
