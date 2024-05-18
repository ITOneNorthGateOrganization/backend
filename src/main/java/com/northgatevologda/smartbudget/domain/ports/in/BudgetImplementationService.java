package com.northgatevologda.smartbudget.domain.ports.in;

import com.northgatevologda.smartbudget.domain.exception.NotFoundException;
import com.northgatevologda.smartbudget.domain.model.BudgetImplementation;

/**
 * This interface represents a service for implementing budget implementations.
 */
public interface BudgetImplementationService {

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
     * @return the unfinished budget implementation found
     * @throws NotFoundException if no unfinished budget implementation is found for the specified ID
     */
    BudgetImplementation findUnfinishedByBudgetId(Long budgetId);
}
