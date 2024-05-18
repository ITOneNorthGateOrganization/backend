package com.northgatevologda.smartbudget.application.service.budget;

import com.northgatevologda.smartbudget.application.service.budget.dto.BudgetDTO;
import com.northgatevologda.smartbudget.domain.exception.BadRequestException;
import com.northgatevologda.smartbudget.domain.exception.ForbiddenException;
import com.northgatevologda.smartbudget.domain.exception.NotFoundException;
import com.northgatevologda.smartbudget.domain.model.*;
import com.northgatevologda.smartbudget.domain.ports.in.*;
import com.northgatevologda.smartbudget.domain.ports.out.BudgetRepositoryPort;
import com.northgatevologda.smartbudget.infrastructure.utils.CronDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Implementation of {@link BudgetService}
 */
@Service
public class BudgetServiceImpl implements BudgetService {
    private static final Logger logger = LoggerFactory.getLogger(BudgetServiceImpl.class);

    private final BudgetImplementationService budgetImplementationService;
    private final BudgetRepositoryPort budgetRepositoryPort;
    private final UserService userService;
    private final TransactionRecordService transactionRecordService;
    private final CategoryService categoryService;

    public BudgetServiceImpl(
            BudgetImplementationService budgetImplementationService,
            BudgetRepositoryPort budgetRepositoryPort,
            UserService userService,
            CategoryService categoryService,
            @Lazy TransactionRecordService transactionRecordService
    ) {
        this.budgetImplementationService = budgetImplementationService;
        this.budgetRepositoryPort = budgetRepositoryPort;
        this.userService = userService;
        this.categoryService = categoryService;
        this.transactionRecordService = transactionRecordService;
    }

    @Override
    public void implementSuitableBudgets() {
        logger.info("Starting implementation of suitable budgets");
        List<Budget> notClosedBudgets = budgetRepositoryPort.findNotClosedBudgets();
        for (Budget budget : notClosedBudgets) {
            processBudget(budget);
        }
    }

    private void processBudget(Budget budget) {
        logger.debug("Processing budget with ID: {}", budget.getId());
        Instant plannedPeriodEnd = CronDateUtils.getNextInstantFromCron(budget.getCron());
        BudgetImplementation currentBudgetImpl =
                budgetImplementationService.findUnfinishedByBudgetId(budget.getId());
        if (plannedPeriodEnd == null ||
                currentBudgetImpl.getPlannedPeriodEnd().isAfter(plannedPeriodEnd)) {
            return;
        }
        closeCurrentImplementation(currentBudgetImpl);
        startNewBudgetImplementation(budget, plannedPeriodEnd);
    }

    private void closeCurrentImplementation(BudgetImplementation budgetImplementation) {
        logger.debug("Closing current implementation for budget ID: {}", budgetImplementation.getBudget().getId());
        Instant startDate = budgetImplementation.getPeriodStart();
        Instant endDate = budgetImplementation.getPlannedPeriodEnd();
        List<TransactionRecord> transactionRecords =
                transactionRecordService.findTransactionRecordsByBudgetIdAndPeriod(
                        budgetImplementation.getBudget().getId(), startDate, endDate);
        BigDecimal amount = getAmountByTransactionRecords(transactionRecords);
        budgetImplementation.setAmount(amount);
        budgetImplementation.setPeriodEnd(Instant.now());
        budgetImplementationService.save(budgetImplementation);
    }

    private void startNewBudgetImplementation(Budget budget, Instant plannedPeriodEnd) {
        logger.debug("Starting new implementation for budget ID: {}", budget.getId());
        BudgetImplementation newBudgetImplementation = BudgetImplementation.builder()
                .budget(budget)
                .limit(budget.getLimit())
                .periodStart(Instant.now())
                .plannedPeriodEnd(plannedPeriodEnd)
                .build();
        budgetImplementationService.save(newBudgetImplementation);
    }

    @Override
    public void implementForciblyBudget(String username, Long budgetId) {
        Budget budget = findBudgetById(budgetId);
        if (budget.isClose()) {
            logger.warn("Budget with ID {} has already been closed", budget.getId());
            throw new BadRequestException("This budget has already been closed");
        }
        if (!budget.getUser().getUsername().equals(username)) {
            throw new ForbiddenException("It is forbidden to close budget to someone else's budget");
        }
        if (budget.getCron() == null) {
            implementNotPeriodicBudget(budget);
        } else {
            implementPeriodicBudget(budget);
        }
    }

    private void implementNotPeriodicBudget(Budget budget) {
        BigDecimal amountTransactions = getAmountByTransactionRecords(budget.getTransactionRecords());
        BudgetImplementation budgetImplementation = BudgetImplementation.builder()
                .amount(amountTransactions)
                .budget(budget)
                .limit(budget.getLimit())
                .periodStart(Instant.now())
                .periodEnd(Instant.now())
                .build();
        logger.info("Saving budget implementation for budget ID {}: {}", budget.getId(), budgetImplementation);
        budgetImplementationService.save(budgetImplementation);
        budget.setClose(true);
        budgetRepositoryPort.save(budget);
    }

    private void implementPeriodicBudget(Budget budget) {
        BudgetImplementation budgetImplementation =
                budgetImplementationService.findUnfinishedByBudgetId(budget.getId());
        Instant startDate = budgetImplementation.getPeriodStart();
        Instant endDate = Instant.now();
        Long budgetId = budget.getId();
        List<TransactionRecord> transactionRecords = transactionRecordService
                .findTransactionRecordsByBudgetIdAndPeriod(budgetId, startDate, endDate);
        BigDecimal amountTransactions = getAmountByTransactionRecords(transactionRecords);
        budgetImplementation.setAmount(amountTransactions);
        budgetImplementation.setPeriodEnd(Instant.now());
        logger.info("Updating budget implementation for budget ID {}: {}", budget.getId(), budgetImplementation);
        budgetImplementationService.save(budgetImplementation);
        budget.setClose(true);
        budgetRepositoryPort.save(budget);
    }

    private BigDecimal getAmountByTransactionRecords(List<TransactionRecord> transactionRecords) {
        return transactionRecords.stream()
                .map(TransactionRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void tieUpCategories(String username, Long budgetId, List<Long> categoriesId) {
        logger.info("Tying up categories for budget with id: {} for user with username: {}", budgetId, username);
        Budget foundBudget = findBudgetById(budgetId);
        if (!foundBudget.getUser().getUsername().equals(username)) {
            throw new ForbiddenException("It is forbidden to tie categories to someone else's budget");
        }
        for (Long categoryId : categoriesId) {
            Category foundCategory = categoryService.findCategoryById(categoryId);
            if (budgetRepositoryPort.checkTieUppedCategory(budgetId, foundCategory.getId())) {
                continue;
            }
            budgetRepositoryPort.tieUpCategory(budgetId, foundCategory.getId());
        }
    }

    @Override
    public Budget findBudgetById(Long budgetId) {
        logger.debug("Retrieving budget by id : {}", budgetId);
        return budgetRepositoryPort.findBudgetById(budgetId)
                .orElseThrow(() -> new NotFoundException("Budget not found by id " + budgetId));
    }

    @Override
    public Budget save(String username, BudgetDTO budgetDTO) {
        logger.info("Creating budget for user with username: {}", username);
        User foundUser = userService.findByUsername(username);
        Budget budgetForCreate = Budget.builder()
                .limit(budgetDTO.getLimit())
                .importance(budgetDTO.getImportance())
                .purpose(budgetDTO.getPurpose())
                .cron(budgetDTO.getCron())
                .user(foundUser)
                .build();
        Budget createdBudget = budgetRepositoryPort.save(budgetForCreate);
        if (createdBudget.getCron() != null) {
            createBudgetImplementationForBudget(createdBudget);
        }
        logger.info("Budget created successfully for user with username: {}", username);
        return createdBudget;
    }

    private void createBudgetImplementationForBudget(Budget budget) {
        Instant plannedPeriodEnd = CronDateUtils.getNextInstantFromCron(budget.getCron());
        BudgetImplementation budgetImplementation = BudgetImplementation.builder()
                .budget(budget)
                .limit(budget.getLimit())
                .periodStart(Instant.now())
                .plannedPeriodEnd(plannedPeriodEnd)
                .build();
        logger.info("Creating budget implementation for budget ID {}: {}", budget.getId(), budgetImplementation);
        budgetImplementationService.save(budgetImplementation);
    }

    @Override
    public List<Budget> findBudgetsByUsername(String username) {
        logger.info("Retrieving all budgets for user with username: {}", username);
        User foundUser = userService.findByUsername(username);
        return budgetRepositoryPort.findBudgetsByUserId(foundUser.getId());
    }

    @Override
    public Budget update(String username, BudgetDTO budgetDTO) {
        logger.info("Updating budget with ID: {} for user with username: {}", budgetDTO.getId(), username);
        Budget foundBudget = findBudgetById(budgetDTO.getId());
        if (!foundBudget.getUser().getUsername().equals(username)) {
            throw new ForbiddenException("It is forbidden to change someone else's budget");
        }
        if (foundBudget.isClose()) {
            throw new BadRequestException("You cannot change the closed budget");
        }
        foundBudget.setLimit(budgetDTO.getLimit());
        foundBudget.setImportance(budgetDTO.getImportance());
        foundBudget.setPurpose(budgetDTO.getPurpose());
        foundBudget.setCron(budgetDTO.getCron());
        Budget updatedBudget = budgetRepositoryPort.save(foundBudget);
        logger.info("Budget updated successfully with ID: {}", updatedBudget.getId());
        return updatedBudget;
    }

    @Override
    public void delete(String username, Long budgetId) {
        logger.info("Deleting budget with ID: {} for user with username: {}", budgetId, username);
        Budget foundBudget = findBudgetById(budgetId);
        if (!foundBudget.getUser().getUsername().equals(username)) {
            throw new ForbiddenException("It is forbidden to change someone else's budget");
        }
        if (foundBudget.isClose()) {
            throw new BadRequestException("You cannot delete a closed budget");
        }
        budgetRepositoryPort.delete(budgetId);
        logger.info("Budget deleted successfully with ID: {}", budgetId);
    }
}
