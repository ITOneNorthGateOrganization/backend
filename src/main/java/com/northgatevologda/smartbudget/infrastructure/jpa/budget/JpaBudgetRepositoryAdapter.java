package com.northgatevologda.smartbudget.infrastructure.jpa.budget;

import com.northgatevologda.smartbudget.domain.model.Budget;
import com.northgatevologda.smartbudget.domain.ports.out.BudgetRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link BudgetRepositoryPort}
 */
@Component
@RequiredArgsConstructor
public class JpaBudgetRepositoryAdapter implements BudgetRepositoryPort {
    private static final Logger logger = LoggerFactory.getLogger(JpaBudgetRepositoryAdapter.class);

    private final JpaBudgetRepository jpaBudgetRepository;

    @Override
    public List<Budget> findNotClosedBudgets() {
        logger.debug("Finding not closed budgets.");
        return jpaBudgetRepository.findBudgetsByClose(false);
    }

    @Override
    public boolean checkTieUppedCategory(Long budgetId, Long categoryId) {
        logger.debug("Checking if category is tied to a budget: Budget ID = {}, Category ID = {}",
                budgetId, categoryId);
        return jpaBudgetRepository.checkTieUppedCategory(budgetId, categoryId);
    }

    @Override
    public void tieUpCategory(Long budgetId, Long categoryId) {
        logger.info("Tying category with id {} to budget with id {}", categoryId, budgetId);
        jpaBudgetRepository.tieUpCategory(budgetId, categoryId);
    }

    @Override
    public Optional<Budget> findBudgetById(Long id) {
        logger.debug("Finding budget by id : {}", id);
        return jpaBudgetRepository.findById(id);
    }

    @Override
    public Budget save(Budget budget) {
        logger.info("Saving budget with ID: {}", budget.getId());
        return jpaBudgetRepository.save(budget);
    }

    @Override
    public List<Budget> findBudgetsByUserId(Long userId) {
        logger.info("Retrieving all budgets for user with ID: {}", userId);
        return jpaBudgetRepository.findBudgetsByUserId(userId);
    }

    @Override
    public void delete(Long budgetId) {
        logger.info("Deleting budget with ID: {}", budgetId);
        jpaBudgetRepository.deleteById(budgetId);
    }
}
