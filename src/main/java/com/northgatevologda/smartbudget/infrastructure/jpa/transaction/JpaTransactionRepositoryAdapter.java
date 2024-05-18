package com.northgatevologda.smartbudget.infrastructure.jpa.transaction;

import com.northgatevologda.smartbudget.domain.model.Transaction;
import com.northgatevologda.smartbudget.domain.ports.out.TransactionRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link TransactionRepositoryPort}
 */
@RequiredArgsConstructor
@Component
public class JpaTransactionRepositoryAdapter implements TransactionRepositoryPort {
    private static final Logger logger = LoggerFactory.getLogger(JpaTransactionRepositoryAdapter.class);

    private final JpaTransactionRepository jpaTransactionRepository;

    @Override
    public Transaction save(Transaction transaction) {
        logger.info("Creating transaction in the repository");
        Transaction createdTransaction = jpaTransactionRepository.save(transaction);
        logger.info("Transaction created successfully");
        return createdTransaction;
    }

    @Override
    public Optional<Transaction> findTransactionById(Long id) {
        logger.info("Finding transaction by ID: {}", id);
        Optional<Transaction> foundTransaction = jpaTransactionRepository.findById(id);
        if (foundTransaction.isPresent()) {
            logger.info("Transaction found for ID: {}", id);
        } else {
            logger.info("No transaction found for ID: {}", id);
        }
        return foundTransaction;
    }

    @Override
    public List<Transaction> findTransactionsByUserId(Long userId) {
        logger.info("Retrieving transactions by user ID: {}", userId);
        return jpaTransactionRepository.findTransactionsByUserId(userId);
    }

    @Override
    public List<Transaction> findTransactionsByUserIdAndAccountId(Long userId, Long accountId) {
        logger.info("Retrieving transactions by account ID: {} and user with id: {}", accountId, userId);
        return jpaTransactionRepository.findTransactionsByUserIdAndAccountId(userId, accountId);
    }

    @Override
    public List<Transaction> findUnaccountedTransactionsByUserId(Long userId) {
        logger.info("Retrieving unaccounted transactions by user ID: {}", userId);
        return jpaTransactionRepository.findUnaccountedTransactionsByUserId(userId);
    }

    @Override
    public List<Transaction> findTransactionsByUserIdAndBudgetId(Long userId, Long budgetId) {
        logger.info("Retrieving transactions by budget ID: {} and user ID: {}", budgetId, userId);
        return jpaTransactionRepository.findTransactionsByUserIdAndBudgetId(userId, budgetId);
    }

    @Override
    public void changeDescription(Long transactionId, String newDescription) {
        logger.info("Changing description for transaction with ID: {}", transactionId);
        jpaTransactionRepository.changeDescription(transactionId, newDescription);
        logger.info("Description updated successfully for transaction ID: {}", transactionId);
    }
}
