package com.northgatevologda.smartbudget.infrastructure.jpa.reports;


import com.northgatevologda.smartbudget.domain.model.CategoryReportProjection;
import com.northgatevologda.smartbudget.domain.ports.out.CategoryReportRepositoryPort;
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
 * Implementation of {@link CategoryReportRepositoryPort}
 */
@Component
@RequiredArgsConstructor
public class JpaCategoryReportAdapter implements CategoryReportRepositoryPort {
    private static final Logger logger = LoggerFactory.getLogger(JpaCategoryReportAdapter.class);

    private final JpaCategoryReportRepository jpaCategoryReportRepository;

    @Override
    public List<CategoryReportProjection> getReport(String username, Instant startDate, Instant endDate, Double step) {
        logger.info("Generating categories report: {} {} {} {}", username, startDate, endDate, step);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return jpaCategoryReportRepository.getReport(
                username,
                formatter.format(startDate),
                formatter.format(endDate),
                BigDecimal.valueOf(step));
    }
}
