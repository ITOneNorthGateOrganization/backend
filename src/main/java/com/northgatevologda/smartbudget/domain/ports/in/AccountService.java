package com.northgatevologda.smartbudget.domain.ports.in;

import com.northgatevologda.smartbudget.application.service.account.dto.AccountDTO;
import com.northgatevologda.smartbudget.domain.model.Account;

import java.util.List;

/**
 * This interface defines the methods for managing user accounts.
 */
public interface AccountService {

    /**
     * Find all accounts associated with the given username.
     *
     * @param username The username of the user whose accounts to find.
     * @return A list of accounts associated with the given username.
     */
    List<Account> findAccounts(String username);

    /**
     * Save a new account for the given user.
     *
     * @param username   The username of the user for whom to save the account.
     * @param accountDTO The DTO object containing the details of the account to save.
     * @return The saved account.
     */
    Account save(String username, AccountDTO accountDTO);

    /**
     * Close the account with the given account ID for the specified user.
     *
     * @param username  The username of the user who owns the account to be closed.
     * @param accountId The ID of the account to be closed.
     */
    void closeAccount(String username, Long accountId);

    /**
     * Find an account by user ID and account ID.
     *
     * @param userId    The ID of the user.
     * @param accountId The ID of the account.
     * @return The account found, or null if not found.
     */
    Account findAccountByUserIdAndAccountId(Long userId, Long accountId);

    /**
     * Find an account by its ID.
     *
     * @param id The ID of the account to find.
     * @return The account found, or null if not found.
     */
    Account findAccountById(Long id);
}
