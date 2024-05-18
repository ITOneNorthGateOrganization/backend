package com.northgatevologda.smartbudget.infrastructure.web.controllers.budget;

import com.northgatevologda.smartbudget.application.service.budget.dto.BudgetDTO;
import com.northgatevologda.smartbudget.application.service.budget.mapper.BudgetServiceMapper;
import com.northgatevologda.smartbudget.domain.model.Budget;
import com.northgatevologda.smartbudget.domain.ports.in.BudgetService;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.budget.dto.BudgetCreateRequest;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.budget.dto.BudgetUpdateRequest;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.budget.mapper.BudgetControllerMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class BudgetController {
    private static final Logger logger = LoggerFactory.getLogger(BudgetController.class);

    private final BudgetServiceMapper budgetServiceMapper;
    private final BudgetControllerMapper budgetControllerMapper;
    private final BudgetService budgetService;

    @PutMapping("/budgets/{budgetId}/tieUpCategories")
    public ResponseEntity<String> tieUpCategories(
            @PathVariable Long budgetId,
            @RequestBody List<Long> categoriesId
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Tying up categories {} for budget with id: {} of user with username: {}",
                categoriesId, budgetId, username);
        budgetService.tieUpCategories(username, budgetId, categoriesId);
        logger.info("Categories successfully tied up for budget with id : {}", budgetId);
        return ResponseEntity.ok("The categories were tied to the budget");
    }

    @PostMapping("/budgets")
    public ResponseEntity<BudgetDTO> createBudget(@Valid @RequestBody BudgetCreateRequest budgetCreateRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Creating budget for user with username: {}", username);
        BudgetDTO budgetForCreate = budgetControllerMapper.toBudgetDTO(budgetCreateRequest);
        Budget savedBudget = budgetService.save(username, budgetForCreate);
        logger.info("Budget created successfully for user with username: {}", username);
        return ResponseEntity.ok(budgetServiceMapper.toDTO(savedBudget));
    }

    @GetMapping("/budgets")
    public ResponseEntity<List<BudgetDTO>> findBudgets() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Finding all budgets for user with username: {}", username);
        List<Budget> budgets = budgetService.findBudgetsByUsername(username);
        return ResponseEntity.ok(budgetServiceMapper.toDTOList(budgets));
    }

    @PutMapping("/budgets")
    public ResponseEntity<BudgetDTO> updateBudget(@Valid @RequestBody BudgetUpdateRequest budgetUpdateRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Updating budget for user with username: {}", username);
        BudgetDTO budgetForUpdate = budgetControllerMapper.toBudgetDTO(budgetUpdateRequest);
        Budget updatedBudget = budgetService.update(username, budgetForUpdate);
        return ResponseEntity.ok(budgetServiceMapper.toDTO(updatedBudget));
    }

    @DeleteMapping("/budgets/{budgetId}")
    public ResponseEntity<String> deleteBudget(@PathVariable Long budgetId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Deleting budget with id: {} for user with username: {}", budgetId, username);
        budgetService.delete(username, budgetId);
        return ResponseEntity.ok("The budget has been successfully deleted");
    }

    @PutMapping("/budgets/{budgetId}/implement")
    public ResponseEntity<String> implementBudget(@PathVariable Long budgetId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Implement budget for user with username: {}", username);
        budgetService.implementForciblyBudget(username, budgetId);
        return ResponseEntity.ok("The budget was implemented successfully");
    }
}
