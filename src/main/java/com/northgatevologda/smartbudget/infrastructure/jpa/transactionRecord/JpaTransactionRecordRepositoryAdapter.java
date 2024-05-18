package com.northgatevologda.smartbudget.infrastructure.jpa.transactionrecord;

import com.northgatevologda.smartbudget.domain.model.TransactionRecord;
import com.northgatevologda.smartbudget.domain.ports.out.TransactionRecordRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link TransactionRecordRepositoryPort}
 */
@RequiredArgsConstructor
@Component
public class JpaTransactionRecordRepositoryAdapter implements TransactionRecordRepositoryPort {
    private static final Logger logger = LoggerFactory.getLogger(JpaTransactionRecordRepositoryAdapter.class);

    private final JpaTransactionRecordRepository transactionRecordRepository;

    @Override
    public List<TransactionRecord> findTransactionRecordsByBudgetIdAndPeriod(
            Long budgetId,
            Instant startDate,
            Instant endDate
    ) {
        logger.info("Finding transaction records for budget ID: {} and period from {} to {}",
                budgetId, startDate, endDate);
        return transactionRecordRepository
                .findTransactionRecordsByBudgetIdAndPeriod(budgetId, startDate, endDate);
    }

    @Override
    public TransactionRecord save(TransactionRecord transactionRecord) {
        logger.info("Saving transaction record in repository");
        TransactionRecord savedTransactionRecord = transactionRecordRepository.save(transactionRecord);
        logger.info("Transaction record saved successfully");
        return savedTransactionRecord;
    }

    @Override
    public List<TransactionRecord> findTransactionRecordsByTransactionId(Long transactionId) {
        logger.info("Finding transaction records by transaction ID: {}", transactionId);
        return transactionRecordRepository.getTransactionRecordsByTransactionId(transactionId);
    }

    @Override
    public void deleteTransactionRecordById(Long id) {
        logger.info("Deleting transaction record with ID: {}", id);
        transactionRecordRepository.deleteById(id);
        logger.info("Transaction record deleted successfully");
    }

    @Override
    public Optional<TransactionRecord> findById(Long id) {
        logger.info("Finding transaction record by ID: {}", id);
        Optional<TransactionRecord> foundTransactionRecord = transactionRecordRepository.findById(id);
        if (foundTransactionRecord.isPresent()) {
            logger.info("Transaction record found for ID: {}", id);
        } else {
            logger.info("No transaction record found for ID: {}", id);
        }
        return foundTransactionRecord;
    }
}
