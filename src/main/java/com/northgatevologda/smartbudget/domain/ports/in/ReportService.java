package com.northgatevologda.smartbudget.domain.ports.in;

import com.northgatevologda.smartbudget.application.service.reports.dto.BankrollChangesReportDTO;
import com.northgatevologda.smartbudget.application.service.reports.dto.BudgetProportionsReportDTO;
import com.northgatevologda.smartbudget.application.service.reports.dto.CategorySpendReportDTO;

import java.time.Instant;

/**
 * Interface for generating reports.
 */
public interface ReportService {
    /**
     * Returns the sum of costs by user's category for a time period, divided into intervals with a distance of step
     *
     * @param username  Username
     * @param startDate Start date of period
     * @param endDate   End date of period
     * @param step      Distance between intervals, specified in seconds
     * @return CategorySpendReportDTO
     */
    CategorySpendReportDTO getCategoriesReport(String username, Instant startDate, Instant endDate, Double step);

    /**
     * Returns the sum of bankroll changes on user's account for a time period, divided into intervals with a distance of step
     *
     * @param username
     * @param startDate
     * @param endDate
     * @param step
     * @return BankrollChangesReportDTO
     */
    BankrollChangesReportDTO getBankrollChangesReport(String username, Instant startDate, Instant endDate, Double step);

    /**
     * Returns the budget implementation percentage report
     *
     * @param username
     * @param startDate
     * @param endDate
     * @return BudgetProportionsReportDTO
     */
    BudgetProportionsReportDTO getBudgetProportionsReport(String username, Instant startDate, Instant endDate);
}
