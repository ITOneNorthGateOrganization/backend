package com.northgatevologda.smartbudget.application.service.reports.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
public class BudgetProportionsReportDTO {
    private Instant startDate;
    private BigDecimal amount;
    private Instant endDate;
    private List<BudgetImplementationDTO> budgets;
}
