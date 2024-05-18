package com.northgatevologda.smartbudget.application.service.transactionrecord;

import com.northgatevologda.smartbudget.application.service.transactionrecord.dto.TransactionRecordDTO;
import com.northgatevologda.smartbudget.application.service.transactionrecord.mapper.TransactionRecordServiceMapper;
import com.northgatevologda.smartbudget.domain.exception.ForbiddenException;
import com.northgatevologda.smartbudget.domain.exception.NotFoundException;
import com.northgatevologda.smartbudget.domain.model.*;
import com.northgatevologda.smartbudget.domain.ports.in.*;
import com.northgatevologda.smartbudget.domain.ports.out.TransactionRecordRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class TransactionRecordServiceImpl implements TransactionRecordService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionRecordServiceImpl.class);

    private final TransactionRecordServiceMapper transactionRecordServiceMapper;
    private final TransactionRecordRepositoryPort transactionRecordRepositoryPort;
    private final CategoryService categoryService;
    private final BudgetService budgetService;
    private final UserService userService;
    private final TransactionService transactionService;

    public TransactionRecordServiceImpl(
            TransactionRecordServiceMapper transactionRecordServiceMapper,
            TransactionRecordRepositoryPort transactionRecordRepositoryPort,
            CategoryService categoryService,
            BudgetService budgetService,
            UserService userService,
            @Lazy TransactionService transactionService
    ) {
        this.transactionRecordServiceMapper = transactionRecordServiceMapper;
        this.transactionRecordRepositoryPort = transactionRecordRepositoryPort;
        this.categoryService = categoryService;
        this.budgetService = budgetService;
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @Override
    public List<TransactionRecord> findTransactionRecordsByBudgetIdAndPeriod(
            Long budgetId,
            Instant startDate,
            Instant endDate
    ) {
        logger.info("Finding transaction records for budget ID: {} and period from {} to {}",
                budgetId, startDate, endDate);
        return transactionRecordRepositoryPort
                .findTransactionRecordsByBudgetIdAndPeriod(budgetId, startDate, endDate);
    }

    @Override
    public List<TransactionRecord> findByUsernameAndTransactionId(String username, Long transactionId) {
        logger.info("Finding transaction records by transaction ID: {} for user with username: {}",
                transactionId, username);
        Transaction transaction = transactionService.findTransactionById(transactionId);
        Account accountFromTransaction = transaction.getAccount();
        if (!accountFromTransaction.getUser().getUsername().equals(username)) {
            throw new ForbiddenException("You can only view your transactions");
        }
        return transactionRecordRepositoryPort.findTransactionRecordsByTransactionId(transactionId);
    }

    @Override
    public void deleteTransactionRecordById(String username, Long id) {
        logger.info("Deleting transaction record with ID: {} for user with username: {}", id, username);
        TransactionRecord transactionRecord = findById(id);
        Transaction transaction = transactionRecord.getTransaction();
        Account account = transaction.getAccount();
        if (!account.getUser().getUsername().equals(username)) {
            throw new ForbiddenException("You can only delete your transaction records");
        }
        transactionRecordRepositoryPort.deleteTransactionRecordById(id);
        logger.info("Transaction record deleted successfully");
    }

    @Override
    public TransactionRecord findById(Long id) {
        logger.info("Finding transaction record with ID {}", id);
        return transactionRecordRepositoryPort.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction record was not found by id " + id));
    }

    @Override
    public TransactionRecord save(String username, Long transactionId, TransactionRecordDTO transactionRecordDTO) {
        logger.info("Saving transaction record for transaction ID: {} for user with username: {}", transactionId, username);
        User user = userService.findByUsername(username);
        Category foundCategory = categoryService.findCategoryById(transactionRecordDTO.getCategoryId());
        Budget foundBudget = budgetService.findBudgetById(transactionRecordDTO.getBudgetId());
        Transaction foundTransaction = transactionService.findTransactionById(transactionId);
        if (checkBudgetLimitTransactionSum(foundTransaction, transactionRecordDTO.getAmount())) {
            throw new ForbiddenException("You cannot add records for an amount greater than the transaction amount");
        }
        Account accountFromTransaction = foundTransaction.getAccount();
        if (accountNotBelongToUser(accountFromTransaction, username)) {
            throw new ForbiddenException("It is forbidden to add an entry to another user's transaction");
        }
        if (categoryNotExistsForUserId(foundCategory.getId(), user.getId())) {
            throw new ForbiddenException("You can only link your own categories");
        }
        if (budgetNotBelongToUser(foundBudget, username)) {
            throw new ForbiddenException("You can only link your budget");
        }
        TransactionRecord transactionRecord = transactionRecordServiceMapper.toEntity(transactionRecordDTO);
        transactionRecord.setTransaction(foundTransaction);
        transactionRecord.setCategory(foundCategory);
        transactionRecord.setBudget(foundBudget);
        TransactionRecord createdTransactionRecord = transactionRecordRepositoryPort.save(transactionRecord);
        logger.info("Transaction record saved successfully");
        return createdTransactionRecord;
    }

    @Override
    public TransactionRecord update(
            String username,
            Long transactionRecordId,
            TransactionRecordDTO newTransactionRecordDTO
    ) {
        logger.info("Updating transaction record by ID: {} for user with username: {}", transactionRecordId, username);
        Category foundCategory = categoryService.findCategoryById(newTransactionRecordDTO.getCategoryId());
        Budget foundBudget = budgetService.findBudgetById(newTransactionRecordDTO.getBudgetId());
        User user = userService.findByUsername(username);
        if (categoryNotExistsForUserId(newTransactionRecordDTO.getCategoryId(), user.getId())) {
            throw new ForbiddenException("You can only link your own categories");
        }
        if (budgetNotBelongToUser(foundBudget, username)) {
            throw new ForbiddenException("You can only link your budget");
        }
        TransactionRecord transactionRecord = findById(transactionRecordId);
        transactionRecord.setCategory(foundCategory);
        transactionRecord.setBudget(foundBudget);
        TransactionRecord savedTransactionRecord = transactionRecordRepositoryPort.save(transactionRecord);
        logger.info("Transaction record updated successfully");
        return savedTransactionRecord;
    }

    private boolean checkBudgetLimitTransactionSum(Transaction transaction, BigDecimal newAmount) {
        BigDecimal totalAmount = transaction.getTransactionRecords().stream()
                .map(TransactionRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return transaction.getAmount().compareTo(totalAmount.add(newAmount)) < 0;
    }

    private boolean categoryNotExistsForUserId(Long categoryId, Long userId) {
        return categoryService.notExistsCategoryByIdForUserId(categoryId, userId);
    }

    private boolean accountNotBelongToUser(Account account, String username) {
        return !account.getUser().getUsername().equals(username);
    }

    private boolean budgetNotBelongToUser(Budget budget, String username) {
        return !budget.getUser().getUsername().equals(username);
    }
}