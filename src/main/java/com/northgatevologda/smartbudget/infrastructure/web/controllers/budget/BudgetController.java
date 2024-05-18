package com.northgatevologda.smartbudget.infrastructure.web.controllers.budget;

import com.northgatevologda.smartbudget.application.service.budget.dto.BudgetDTO;
import com.northgatevologda.smartbudget.application.service.budget.mapper.BudgetServiceMapper;
import com.northgatevologda.smartbudget.domain.model.Budget;
import com.northgatevologda.smartbudget.domain.ports.in.BudgetService;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.budget.dto.BudgetCreateRequest;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.budget.dto.BudgetUpdateRequest;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.budget.mapper.BudgetControllerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Budget Controller", description = "Controller for managing user budgets")
public class BudgetController {
    private static final Logger logger = LoggerFactory.getLogger(BudgetController.class);

    private final BudgetServiceMapper budgetServiceMapper;
    private final BudgetControllerMapper budgetControllerMapper;
    private final BudgetService budgetService;

    @PutMapping(value = "/budgets/{budgetId}/tieUpCategories", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Tie categories to a budget",
            description = "Associates a list of category IDs to a specified budget for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categories successfully tied to the budget"),
                    @ApiResponse(responseCode = "400", description = "Invalid input or missing category/budget IDs"),
                    @ApiResponse(responseCode = "404", description = "Budget or category not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<String> tieUpCategories(
            @Parameter(description = "ID of the budget to which categories are to be tied", required = true)
            @PathVariable Long budgetId,
            @Parameter(description = "List of category IDs to be tied to the budget", required = true)
            @RequestBody List<Long> categoriesId
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Tying up categories {} for budget with id: {} of user with username: {}",
                categoriesId, budgetId, username);
        budgetService.tieUpCategories(username, budgetId, categoriesId);
        logger.info("Categories successfully tied up for budget with id : {}", budgetId);
        return ResponseEntity.ok("The categories were tied to the budget");
    }

    @PostMapping(value = "/budgets", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new budget",
            description = "Creates a new budget using the provided budget request data for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Budget created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid budget creation request data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<BudgetDTO> createBudget(
            @Parameter(description = "Budget creation request data", required = true)
            @Valid @RequestBody BudgetCreateRequest budgetCreateRequest
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Creating budget for user with username: {}", username);
        BudgetDTO budgetForCreate = budgetControllerMapper.toBudgetDTO(budgetCreateRequest);
        Budget savedBudget = budgetService.save(username, budgetForCreate);
        logger.info("Budget created successfully for user with username: {}", username);
        return ResponseEntity.ok(budgetServiceMapper.toDTO(savedBudget));
    }

    @GetMapping(value = "/budgets", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find all budgets for the authenticated user",
            description = "Fetches a list of all budgets associated with the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved all budgets"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<BudgetDTO>> findBudgets() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Finding all budgets for user with username: {}", username);
        List<Budget> budgets = budgetService.findBudgetsByUsername(username);
        return ResponseEntity.ok(budgetServiceMapper.toDTOList(budgets));
    }

    @PutMapping(value = "/budgets", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a budget",
            description = "Updates an existing budget for the authenticated user based on the provided data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Budget successfully updated"),
                    @ApiResponse(responseCode = "400", description = "Invalid budget update request data"),
                    @ApiResponse(responseCode = "404", description = "Budget not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<BudgetDTO> updateBudget(
            @Parameter(description = "Budget update request data", required = true)
            @Valid @RequestBody BudgetUpdateRequest budgetUpdateRequest
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Updating budget for user with username: {}", username);
        BudgetDTO budgetForUpdate = budgetControllerMapper.toBudgetDTO(budgetUpdateRequest);
        Budget updatedBudget = budgetService.update(username, budgetForUpdate);
        return ResponseEntity.ok(budgetServiceMapper.toDTO(updatedBudget));
    }

    @DeleteMapping(value = "/budgets/{budgetId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Delete a budget",
            description = "Deletes a specific budget based on the ID provided for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The budget has been successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Budget not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized to perform this operation"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<String> deleteBudget(
            @Parameter(description = "The ID of the budget to delete", required = true)
            @PathVariable Long budgetId
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Deleting budget with id: {} for user with username: {}", budgetId, username);
        budgetService.delete(username, budgetId);
        return ResponseEntity.ok("The budget has been successfully deleted");
    }

    @PutMapping(value = "/budgets/{budgetId}/implement", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Implement a budget",
            description = "Forcibly implements a specified budget for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The budget was implemented successfully"),
                    @ApiResponse(responseCode = "404", description = "Budget not found"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized to perform this action"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<String> implementBudget(
            @Parameter(description = "The ID of the budget to implement", required = true)
            @PathVariable Long budgetId
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Implement budget for user with username: {}", username);
        budgetService.implementForciblyBudget(username, budgetId);
        return ResponseEntity.ok("The budget was implemented successfully");
    }
}
