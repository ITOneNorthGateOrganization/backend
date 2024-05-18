package com.northgatevologda.smartbudget.application.service.budgetimplementation;

import com.northgatevologda.smartbudget.domain.exception.NotFoundException;
import com.northgatevologda.smartbudget.domain.model.BudgetImplementation;
import com.northgatevologda.smartbudget.domain.ports.in.BudgetImplementationService;
import com.northgatevologda.smartbudget.domain.ports.out.BudgetImplementationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BudgetImplementationServiceImpl implements BudgetImplementationService {
    private static final Logger logger = LoggerFactory.getLogger(BudgetImplementationServiceImpl.class);

    private final BudgetImplementationRepositoryPort budgetImplementationRepositoryPort;

    @Override
    public BudgetImplementation save(BudgetImplementation budgetImplementation) {
        logger.info("Saving budget implementation: {}", budgetImplementation);
        BudgetImplementation savedBudgetImplementation = budgetImplementationRepositoryPort.save(budgetImplementation);
        logger.info("Budget implementation saved: {}", savedBudgetImplementation);
        return savedBudgetImplementation;
    }

    @Override
    public BudgetImplementation findUnfinishedByBudgetId(Long budgetId) {
        logger.info("Finding unfinished budget implementation for budget ID: {}", budgetId);
        return budgetImplementationRepositoryPort.findUnfinishedByBudgetId(budgetId)
                .orElseThrow(() -> new NotFoundException("An unfinished budget was not found by id " + budgetId));
    }
}
