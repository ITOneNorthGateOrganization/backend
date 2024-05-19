package com.northgatevologda.smartbudget.application.service.reports.mapper;

import com.northgatevologda.smartbudget.application.service.reports.dto.CategoryReportDTO;
import com.northgatevologda.smartbudget.application.service.reports.dto.CategorySpendDTO;
import com.northgatevologda.smartbudget.application.service.reports.dto.CategorySpendReportDTO;
import com.northgatevologda.smartbudget.domain.model.CategoryReportProjection;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

/**
 * Mapper class for ReportService that converts entities to DTO objects.
 */
@Component
public class CategoryReportMapper {
    /**
     * Converts list of CategoryReport entity to CategorySpendReportDTO data transfer object.
     *
     * @param categoryReports the list of CategoryReport entity
     * @return the CategorySpendReportDTO object
     */
    public CategorySpendReportDTO toDTO(
            Instant startDate,
            Instant endDate,
            Double step,
            List<CategoryReportProjection> categoryReports
    ) {
        Set<CategoryReportDTO> categories = new HashSet<>();
        Map<Long, List<CategorySpendDTO>> groupedReports = new HashMap<>();
        for (CategoryReportProjection report : categoryReports) {
            groupedReports.computeIfAbsent(report.getCategoryId(), k -> new ArrayList<>()).add(toCategorySpendDTO(report));
            categories.add(toCategoryReportDTO(report));
        }

        for (CategoryReportDTO oldReport : categories) {
            List<CategorySpendDTO> spends = groupedReports.get(oldReport.getCategoryId());
            oldReport.setSpends(spends);
        }

        return CategorySpendReportDTO.builder()
                .startDate(startDate)
                .endDate(endDate)
                .step(step)
                .categories(categories)
                .build();
    }

    private CategoryReportDTO toCategoryReportDTO(CategoryReportProjection report) {
        return CategoryReportDTO.builder()
                .categoryId(report.getCategoryId())
                .name(report.getCategoryName())
                .spends(new ArrayList<>())
                .build();
    }

    private CategorySpendDTO toCategorySpendDTO(CategoryReportProjection categoryReport) {
        return CategorySpendDTO.builder()
                .period(categoryReport.getPeriodStart())
                .amount(categoryReport.getAmount())
                .build();
    }
}
