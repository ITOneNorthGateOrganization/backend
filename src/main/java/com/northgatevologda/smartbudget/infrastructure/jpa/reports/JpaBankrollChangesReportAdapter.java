package com.northgatevologda.smartbudget.infrastructure.jpa.reports;

import com.northgatevologda.smartbudget.domain.model.BankrollChangesReportProjection;
import com.northgatevologda.smartbudget.domain.ports.out.BankrollChangesRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Implementation of {@link BankrollChangesRepositoryPort}
 */
@Component
@RequiredArgsConstructor
public class JpaBankrollChangesReportAdapter implements BankrollChangesRepositoryPort {
    private static final Logger logger = LoggerFactory.getLogger(JpaBankrollChangesReportAdapter.class);

    private final JpaBankrollChangesReportRepository jpaBankrollChangesReportRepository;

    @Override
    public List<BankrollChangesReportProjection> getReport(String username, Instant startDate, Instant endDate, Double step) {
        logger.info("Generating bankroll changes report: {} {} {} {}", username, startDate, endDate, step);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            .withZone( ZoneId.systemDefault());
        return jpaBankrollChangesReportRepository.getReport(
            username,
            formatter.format(startDate),
            formatter.format(endDate),
            BigDecimal.valueOf(step)
        );
    }
}
