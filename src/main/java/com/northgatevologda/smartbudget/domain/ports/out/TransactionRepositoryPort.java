package com.northgatevologda.smartbudget.domain.ports.out;

import com.northgatevologda.smartbudget.domain.model.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * Interface for the port responsible for creating Transaction objects in a repository.
 */
public interface TransactionRepositoryPort {

    /**
     * Save a transaction.
     *
     * @param transaction The transaction to save.
     * @return The saved transaction.
     */
    Transaction save(Transaction transaction);

    /**
     * Find a transaction by its ID.
     *
     * @param id The ID of the transaction to find.
     * @return An Optional containing the transaction found, or empty if not found.
     */
    Optional<Transaction> findTransactionById(Long id);

    /**
     * Find transactions by user ID.
     *
     * @param userId The ID of the user.
     * @return A list of transactions associated with the user ID.
     */
    List<Transaction> findTransactionsByUserId(Long userId);

    /**
     * Find transactions by user ID and account ID.
     *
     * @param id        The ID of the user.
     * @param accountId The ID of the account.
     * @return A list of transactions associated with the user ID and account ID.
     */
    List<Transaction> findTransactionsByUserIdAndAccountId(Long id, Long accountId);

    /**
     * Find unaccounted transactions by user ID.
     *
     * @param userId The ID of the user.
     * @return A list of unaccounted transactions associated with the user ID.
     */
    List<Transaction> findUnaccountedTransactionsByUserId(Long userId);

    /**
     * Find transactions by user ID and budget ID.
     *
     * @param userId   The ID of the user.
     * @param budgetId The ID of the budget.
     * @return A list of transactions associated with the user ID and budget ID.
     */
    List<Transaction> findTransactionsByUserIdAndBudgetId(Long userId, Long budgetId);

    /**
     * Change the description of a transaction.
     *
     * @param transactionId  The ID of the transaction to update.
     * @param newDescription The new description for the transaction.
     */
    void changeDescription(Long transactionId, String newDescription);
}
