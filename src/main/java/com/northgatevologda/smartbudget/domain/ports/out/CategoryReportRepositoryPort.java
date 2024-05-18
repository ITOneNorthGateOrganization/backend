package com.northgatevologda.smartbudget.domain.ports.out;

import com.northgatevologda.smartbudget.domain.model.CategoryReportProjection;

import java.time.Instant;
import java.util.List;

/**
 * This interface defines the methods for interacting with the repository for generating reports.
 */
public interface CategoryReportRepositoryPort {
    /**
     * Generate report about user's category spends
     *
     * @param username  Username
     * @param startDate Start date of period
     * @param endDate   End date of period
     * @param step      Distance between intervals, specified in seconds
     * @return list of CategoryReportProjection
     */
    List<CategoryReportProjection> getReport(String username, Instant startDate, Instant endDate, Double step);
}
