package com.northgatevologda.smartbudget.domain.ports.in;

import com.northgatevologda.smartbudget.application.service.budget.dto.BudgetDTO;
import com.northgatevologda.smartbudget.domain.model.Budget;

import java.util.List;

/**
 * This interface defines the methods for managing budgets.
 */
public interface BudgetService {

    /**
     * Method to implement suitable budgets based on specific criteria.
     */
    void implementSuitableBudgets();

    /**
     * Implements a budget for a specific user.
     *
     * @param username the username of the user
     * @param budgetId the ID of the budget to be implemented
     */
    void implementForciblyBudget(String username, Long budgetId);

    /**
     * Tie up categories with a specified budget.
     *
     * @param username     The username of the user performing the operation.
     * @param budgetId     The ID of the budget to tie up categories with.
     * @param categoriesId The list of category IDs to tie up with the budget.
     */
    void tieUpCategories(String username, Long budgetId, List<Long> categoriesId);

    /**
     * Find a budget by its ID.
     *
     * @param budgetId The ID of the budget to find.
     * @return The budget found, or null if not found.
     */
    Budget findBudgetById(Long budgetId);

    /**
     * Save a new budget for the given user.
     *
     * @param username  The username of the user for whom to save the budget.
     * @param budgetDTO The DTO object containing the details of the budget to save.
     * @return The saved budget.
     */
    Budget save(String username, BudgetDTO budgetDTO);

    /**
     * Find all budgets associated with the given username.
     *
     * @param username The username of the user whose budgets to find.
     * @return A list of budgets associated with the given username.
     */
    List<Budget> findBudgetsByUsername(String username);

    /**
     * Update an existing budget.
     *
     * @param username  The username of the user performing the update.
     * @param budgetDTO The DTO object containing the updated details of the budget.
     * @return The updated budget.
     */
    Budget update(String username, BudgetDTO budgetDTO);

    /**
     * Delete a budget with the given budget ID for the specified user.
     *
     * @param username The username of the user who owns the budget to be deleted.
     * @param budgetId The ID of the budget to be deleted.
     */
    void delete(String username, Long budgetId);
}
