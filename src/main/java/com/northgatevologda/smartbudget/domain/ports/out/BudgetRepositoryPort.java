package com.northgatevologda.smartbudget.domain.ports.out;

import com.northgatevologda.smartbudget.domain.model.Budget;

import java.util.List;
import java.util.Optional;

/**
 * This interface defines the methods for interacting with the repository for managing budgets.
 */
public interface BudgetRepositoryPort {

    /**
     * Method to find budgets that are not closed.
     *
     * @return a list of budgets that are not closed
     */
    List<Budget> findNotClosedBudgets();

    /**
     * Check if a category is tied up with the specified budget.
     *
     * @param budgetId   The ID of the budget.
     * @param categoryId The ID of the category.
     * @return True if the category is tied up with the budget, false otherwise.
     */
    boolean checkTieUppedCategory(Long budgetId, Long categoryId);

    /**
     * Tie up a category with a budget.
     *
     * @param budgetId   The ID of the budget.
     * @param categoryId The ID of the category to tie up.
     */
    void tieUpCategory(Long budgetId, Long categoryId);

    /**
     * Find a budget by its ID.
     *
     * @param id The ID of the budget.
     * @return An Optional containing the budget found, or empty if not found.
     */
    Optional<Budget> findBudgetById(Long id);

    /**
     * Save a budget.
     *
     * @param budget The budget to save.
     * @return The saved budget.
     */
    Budget save(Budget budget);

    /**
     * Find all budgets by user ID.
     *
     * @param userId The ID of the user.
     * @return A list of budgets associated with the user ID.
     */
    List<Budget> findBudgetsByUserId(Long userId);

    /**
     * Delete a budget by ID.
     *
     * @param budgetId The ID of the budget to delete.
     */
    void delete(Long budgetId);
}
