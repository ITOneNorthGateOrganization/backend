package com.northgatevologda.smartbudget.application.service.reports.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class CategorySpendDTO {
    private Instant period;
    private BigDecimal amount;
}
