package com.northgatevologda.smartbudget.domain.ports.in;

import com.northgatevologda.smartbudget.application.service.transactionrecord.dto.TransactionRecordDTO;
import com.northgatevologda.smartbudget.domain.model.TransactionRecord;

import java.time.Instant;
import java.util.List;

/**
 * Service interface for handling transaction records.
 */
public interface TransactionRecordService {

    /**
     * Find transaction records by budget ID and period.
     *
     * @param budgetId  the ID of the transaction
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
     * Find all transaction records by username and transaction ID.
     *
     * @param username      The username of the user.
     * @param transactionId The ID of the transaction.
     * @return A list of transaction records that match the criteria.
     */
    List<TransactionRecord> findByUsernameAndTransactionId(String username, Long transactionId);

    /**
     * Find a transaction record by its ID.
     *
     * @param id The ID of the transaction record to find.
     * @return The transaction record found, or null if not found.
     */
    TransactionRecord findById(Long id);

    /**
     * Delete a transaction record by ID.
     *
     * @param username The username of the user.
     * @param id       The ID of the transaction record to delete.
     */
    void deleteTransactionRecordById(String username, Long id);

    /**
     * Save a new transaction record.
     *
     * @param username             The username of the user.
     * @param transactionId        The ID of the transaction to link the record to.
     * @param transactionRecordDTO The DTO object containing the details of the transaction record to save.
     * @return The saved transaction record.
     */
    TransactionRecord save(String username, Long transactionId, TransactionRecordDTO transactionRecordDTO);

    /**
     * Update an existing transaction record.
     *
     * @param username                The username of the user.
     * @param transactionRecordId     The ID of the transaction record to update.
     * @param newTransactionRecordDTO The DTO object containing the updated details of the transaction record.
     * @return The updated transaction record.
     */
    TransactionRecord update(String username, Long transactionRecordId, TransactionRecordDTO newTransactionRecordDTO);
}
