package com.northgatevologda.smartbudget.application.service.reports;

import com.northgatevologda.smartbudget.application.service.reports.dto.CategorySpendReportDTO;
import com.northgatevologda.smartbudget.application.service.reports.mapper.CategoryReportMapper;
import com.northgatevologda.smartbudget.domain.model.CategoryReportProjection;
import com.northgatevologda.smartbudget.domain.ports.in.ReportService;
import com.northgatevologda.smartbudget.domain.ports.out.CategoryReportRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {
    private static final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    private final CategoryReportRepositoryPort categoryReportRepositoryPort;
    private final CategoryReportMapper categoryReportMapper;

    @Override
    public CategorySpendReportDTO getCategoriesReport(String username, Instant startDate, Instant endDate, Double step) {
        logger.debug("Generating categories report : {} {} {} {}", username, startDate, endDate, step);
        List<CategoryReportProjection> categoryReportList = categoryReportRepositoryPort.getReport(username, startDate, endDate, step);
        return categoryReportMapper.toDTO(startDate, endDate, step, categoryReportList);
    }
}
