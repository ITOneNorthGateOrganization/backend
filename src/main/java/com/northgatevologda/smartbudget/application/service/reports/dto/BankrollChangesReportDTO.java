package com.northgatevologda.smartbudget.application.service.reports.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class BankrollChangesReportDTO {
    private Instant startDate;
    private Instant endDate;
    private Double step;
    private List<BankrollChangesDTO> incomes;
    private List<BankrollChangesDTO> expenses;
    private List<BankrollChangesDTO> balance;
}
