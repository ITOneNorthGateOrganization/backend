package com.northgatevologda.smartbudget.application.service.reports.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BudgetImplementationDTO {
    private Long id;
    private String purpose;
    private BigDecimal amount;
    private Double percent;
}
