package com.northgatevologda.smartbudget.domain.ports.out;

import com.northgatevologda.smartbudget.domain.model.BankrollChangesReportProjection;

import java.time.Instant;
import java.util.List;

/**
 * This interface defines the methods for interacting with the repository for generating reports about user bankroll changes on accounts.
 */
public interface BankrollChangesRepositoryPort {
    /**
     * Return reports about user bankroll changes on accounts.
     *
     * @param username
     * @param startDate
     * @param endDate
     * @param step
     * @return list of BankrollChangesReportProjection
     */
    List<BankrollChangesReportProjection> getReport(String username, Instant startDate, Instant endDate, Double step);
}
