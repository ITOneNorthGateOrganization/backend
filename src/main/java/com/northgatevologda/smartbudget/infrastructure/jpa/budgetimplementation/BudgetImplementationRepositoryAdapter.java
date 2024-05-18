package com.northgatevologda.smartbudget.infrastructure.jpa.budgetimplementation;

import com.northgatevologda.smartbudget.domain.model.BudgetImplementation;
import com.northgatevologda.smartbudget.domain.ports.out.BudgetImplementationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class BudgetImplementationRepositoryAdapter implements BudgetImplementationRepositoryPort {
    private static final Logger logger = LoggerFactory.getLogger(BudgetImplementationRepositoryAdapter.class);

    private final BudgetImplementationRepository budgetImplementationRepository;

    @Override
    public BudgetImplementation save(BudgetImplementation budgetImplementation) {
        logger.info("Saving budget implementation: {}", budgetImplementation);
        BudgetImplementation savedBudgetImplementation = budgetImplementationRepository.save(budgetImplementation);
        logger.info("Budget implementation saved: {}", savedBudgetImplementation);
        return savedBudgetImplementation;
    }

    @Override
    public Optional<BudgetImplementation> findUnfinishedByBudgetId(Long budgetId) {
        logger.info("Finding unfinished budget implementation for budget ID: {}", budgetId);
        return budgetImplementationRepository.findUnfinishedByBudgetId(budgetId);
    }
}
