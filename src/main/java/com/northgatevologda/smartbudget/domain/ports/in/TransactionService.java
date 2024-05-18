package com.northgatevologda.smartbudget.domain.ports.in;

import com.northgatevologda.smartbudget.application.service.transaction.dto.TransactionDTO;
import com.northgatevologda.smartbudget.domain.model.Transaction;

import java.util.List;

/**
 * Service interface for handling transactions.
 */
public interface TransactionService {

    /**
     * Save a new transaction for the specified user and account.
     *
     * @param username       The username of the user.
     * @param accountId      The ID of the account.
     * @param transactionDTO The DTO object containing the details of the transaction to save.
     * @return The saved transaction.
     */
    Transaction save(String username, Long accountId, TransactionDTO transactionDTO);

    /**
     * Find a transaction by its ID.
     *
     * @param id The ID of the transaction to find.
     * @return The transaction found, or null if not found.
     */
    Transaction findTransactionById(Long id);

    /**
     * Find all transactions associated with the given username.
     *
     * @param username The username of the user whose transactions to find.
     * @return A list of transactions associated with the given username.
     */
    List<Transaction> findTransactionsByUsername(String username);

    /**
     * Find all transactions associated with the given username and account ID.
     *
     * @param username  The username of the user.
     * @param accountId The ID of the account.
     * @return A list of transactions associated with the given username and account.
     */
    List<Transaction> findTransactionsByUsernameAndAccountId(String username, Long accountId);

    /**
     * Find all unaccounted transactions associated with the given username.
     *
     * @param username The username of the user.
     * @return A list of unaccounted transactions associated with the given username.
     */
    List<Transaction> findUnaccountedTransactionsByUsername(String username);

    /**
     * Find all transactions associated with the given username and budget ID.
     *
     * @param username The username of the user.
     * @param budgetId The ID of the budget.
     * @return A list of transactions associated with the given username and budget.
     */
    List<Transaction> findTransactionsByUsernameAndBudgetId(String username, Long budgetId);

    /**
     * Change the description of a transaction.
     *
     * @param username       The username of the user.
     * @param transactionId  The ID of the transaction to update.
     * @param newDescription The new description for the transaction.
     */
    void changeDescription(String username, Long transactionId, String newDescription);
}
