package com.northgatevologda.smartbudget.infrastructure.web.reports;

import com.northgatevologda.smartbudget.application.service.reports.dto.BankrollChangesReportDTO;
import com.northgatevologda.smartbudget.application.service.reports.dto.BudgetProportionsReportDTO;
import com.northgatevologda.smartbudget.application.service.reports.dto.CategorySpendReportDTO;
import com.northgatevologda.smartbudget.domain.ports.in.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
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
@Tag(name = "Report Controller", description = "Controller for generating user reports")
public class ReportController {
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
    private final ReportService reportService;

    @Operation(summary = "Get category spend report",
            description = "Generate a report on category spending for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categories report generated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategorySpendReportDTO> getCategorySpendReport(
            @Parameter(description = "Start date for the report", required = true)
            @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date startDate,
            @Parameter(description = "End date for the report", required = true)
            @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date endDate,
            @Parameter(description = "Step size for the report", required = true)
            @RequestParam(value = "step") Double step
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Generating categories report for user with username: {}", username);
        CategorySpendReportDTO report = reportService.getCategoriesReport(
                username, startDate.toInstant(), endDate.toInstant(), step);
        return ResponseEntity.ok(report);
    }

    @Operation(summary = "Get bankroll changes report",
            description = "Generate a report on bankroll changes for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Bankroll changes report generated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @GetMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BankrollChangesReportDTO> getBankrollChangesReport(
            @Parameter(description = "Start date for the report", required = true)
            @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date startDate,
            @Parameter(description = "End date for the report", required = true)
            @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date endDate,
            @Parameter(description = "Step size for the report", required = true)
            @RequestParam(value = "step") Double step
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Generating report about bankroll changes for user with username: {}", username);
        BankrollChangesReportDTO report = reportService.getBankrollChangesReport(
                username, startDate.toInstant(), endDate.toInstant(), step);
        return ResponseEntity.ok(report);
    }

    @Operation(summary = "Get budget proportions report",
            description = "Generates a report showing the budget proportions for a given time period for the authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Budget proportions report"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @GetMapping(value = "/budgets", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BudgetProportionsReportDTO> getBudgetProportionsReport(
            @Parameter(description = "Start date for the report period in ISO datetime format (yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date startDate,
            @Parameter(description = "End date for the report period in ISO datetime format (yyyy-MM-dd'T'HH:mm:ss)", required = true)
            @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date endDate
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Generating budget implementation percentage report for user with username: {}", username);
        BudgetProportionsReportDTO report =
                reportService.getBudgetProportionsReport(
                        username,
                        startDate.toInstant(),
                        endDate.toInstant()
                );
        return ResponseEntity.ok(report);
    }
}
