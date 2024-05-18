package com.northgatevologda.smartbudget.infrastructure.jpa.reports;

import com.northgatevologda.smartbudget.domain.model.BudgetImplementationProjection;
import com.northgatevologda.smartbudget.domain.ports.out.BudgetProportionsReportRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Implementation of {@link BudgetProportionsReportRepositoryPort}
 */
@Component
@RequiredArgsConstructor
public class JpaBudgetProportionsReportAdapter implements BudgetProportionsReportRepositoryPort {

    private static final Logger logger = LoggerFactory.getLogger(JpaBudgetProportionsReportAdapter.class);

    private final JpaBudgetProportionReportRepository jpaBudgetProportionReportRepository;

    @Override
    public List<BudgetImplementationProjection> getReport(String username, Instant startDate, Instant endDate) {
        logger.info("Generating categories report: {} {} {}", username, startDate, endDate);
        return jpaBudgetProportionReportRepository.getReport(
            username,
            startDate,
            endDate);
    }

    @Override
    public BigDecimal getSumImplementation(String username, Instant startDate, Instant endDate) {
        logger.info("Getting sum of budget implementations: {} {} {}", username, startDate, endDate);
        return jpaBudgetProportionReportRepository.getSumImplementation(
            username,
            startDate,
            endDate);
    }
}
