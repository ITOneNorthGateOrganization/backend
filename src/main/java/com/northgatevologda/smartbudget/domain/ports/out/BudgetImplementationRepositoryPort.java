package com.northgatevologda.smartbudget.domain.ports.out;

import com.northgatevologda.smartbudget.domain.model.BudgetImplementation;

import java.util.Optional;

/**
 * This interface represents a port for saving budget implementations.
 */
public interface BudgetImplementationRepositoryPort {

    /**
     * Saves the budget implementation.
     *
     * @param budgetImplementation the budget implementation to be saved
     * @return the saved budget implementation
     */
    BudgetImplementation save(BudgetImplementation budgetImplementation);

    /**
     * Finds the unfinished budget implementation by the specified budget ID.
     *
     * @param budgetId the ID of the budget
     * @return an optional containing the unfinished budget implementation found, or an empty optional if not found
     */
    Optional<BudgetImplementation> findUnfinishedByBudgetId(Long budgetId);
}
