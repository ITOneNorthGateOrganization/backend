package com.northgatevologda.smartbudget.infrastructure.web.reports;

import com.northgatevologda.smartbudget.application.service.reports.dto.BankrollChangesReportDTO;
import com.northgatevologda.smartbudget.application.service.reports.dto.CategorySpendReportDTO;
import com.northgatevologda.smartbudget.domain.ports.in.ReportService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users/reports")
public class ReportController {
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
    private final ReportService reportService;

    @GetMapping("/categories")
    public ResponseEntity<CategorySpendReportDTO> getCategorySpendReport(
        @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date startDate,
        @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date endDate,
        @RequestParam(value = "step") Double step
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Generating categories report for user with username: {}", username);
        CategorySpendReportDTO report =
            reportService.getCategoriesReport(
                username,
                startDate.toInstant(),
                endDate.toInstant(),
                step
            );
        return ResponseEntity.ok(report);
    }

    @GetMapping("/accounts")
    public ResponseEntity<BankrollChangesReportDTO> getBankrollChangesReport(
        @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date startDate,
        @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date endDate,
        @RequestParam(value = "step") Double step
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Generating report about bankroll changes for user with username: {}", username);
        BankrollChangesReportDTO report =
            reportService.getBankrollChangesReport(
                username,
                startDate.toInstant(),
                endDate.toInstant(),
                step
            );
        return ResponseEntity.ok(report);
    }
}
