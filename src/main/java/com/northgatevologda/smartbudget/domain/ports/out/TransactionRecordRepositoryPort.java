package com.northgatevologda.smartbudget.domain.ports.out;

import com.northgatevologda.smartbudget.domain.model.TransactionRecord;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Interface for the port responsible for managing TransactionRecord objects in a repository.
 */
public interface TransactionRecordRepositoryPort {

    /**
     * Find transaction records by budget ID and period.
     *
     * @param budgetId  the ID of the budget
     * @param startDate the start date of the period
     * @param endDate   the end date of the period
     * @return a list of transaction records within the specified period for the given budget ID
     */
    List<TransactionRecord> findTransactionRecordsByBudgetIdAndPeriod(
            Long budgetId,
            Instant startDate,
            Instant endDate
    );

    /**
     * Creates a TransactionRecord object in the repository.
     *
     * @param transactionRecord The TransactionRecord object to be created
     * @return The TransactionRecord object that was created
     */
    TransactionRecord save(TransactionRecord transactionRecord);

    /**
     * Finds transaction records by transaction ID.
     *
     * @param transactionId The ID of the transaction
     * @return List of TransactionRecord objects for the transaction ID
     */
    List<TransactionRecord> findTransactionRecordsByTransactionId(Long transactionId);

    /**
     * Deletes a transaction record by its ID.
     *
     * @param id The ID of the transaction record to delete
     */
    void deleteTransactionRecordById(Long id);

    /**
     * Finds a transaction record by its ID.
     *
     * @param id The ID of the transaction record to find
     * @return TransactionRecord object for the found transaction record
     */
    Optional<TransactionRecord> findById(Long id);
}
