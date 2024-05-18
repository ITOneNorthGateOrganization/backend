package com.northgatevologda.smartbudget.application.service.reports.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
@Builder
public class CategorySpendReportDTO {
    private Instant startDate;
    private Instant endDate;
    private Double step;
    private Set<CategoryReportDTO> categories;
}
