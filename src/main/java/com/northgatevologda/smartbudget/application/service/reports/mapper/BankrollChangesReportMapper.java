package com.northgatevologda.smartbudget.application.service.reports.mapper;

import com.northgatevologda.smartbudget.application.service.reports.dto.BankrollChangesDTO;
import com.northgatevologda.smartbudget.application.service.reports.dto.BankrollChangesReportDTO;
import com.northgatevologda.smartbudget.domain.model.BankrollChangesReportProjection;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapper class for ReportService that converts entities to DTO objects.
 */
@Component
public class BankrollChangesReportMapper {
    public BankrollChangesReportDTO toBankrollChangesReportDTO(
        Instant startDate,
        Instant endDate,
        Double step,
        List<BankrollChangesReportProjection> bankrollChanges
    ) {
        Map<Integer, List<BankrollChangesReportProjection>> changes = bankrollChanges.stream()
            .collect(Collectors.groupingBy(BankrollChangesReportProjection::getTransactionTypeId));

        List<BankrollChangesDTO> incomes = toBankrollChangesDTO(changes.get(BankrollChangesReportProjection.INCOME_TYPE_ID));
        List<BankrollChangesDTO> expenses = toBankrollChangesDTO(changes.get(BankrollChangesReportProjection.EXPENSES_TYPE_ID));
        List<BankrollChangesDTO> balance = toBankrollChangesDTO(changes.get(BankrollChangesReportProjection.BALANCE_TYPE_ID));

        return BankrollChangesReportDTO.builder()
            .step(step)
            .startDate(startDate)
            .endDate(endDate)
            .incomes(incomes)
            .expenses(expenses)
            .balance(balance)
            .build();
    }

    private List<BankrollChangesDTO> toBankrollChangesDTO(List<BankrollChangesReportProjection> projections) {
        if (projections == null) {
            return List.of();
        }
        return projections.stream().map(projection ->
            BankrollChangesDTO.builder()
                .period(projection.getPeriodStart())
                .amount(projection.getAmount())
                .build()
        ).collect(Collectors.toList());
    }
}
