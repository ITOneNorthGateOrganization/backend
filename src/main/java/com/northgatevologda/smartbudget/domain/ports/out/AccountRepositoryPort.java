package com.northgatevologda.smartbudget.domain.ports.out;

import com.northgatevologda.smartbudget.domain.model.Account;

import java.util.List;
import java.util.Optional;

/**
 * This interface defines the methods for interacting with the repository for managing accounts.
 */
public interface AccountRepositoryPort {

    /**
     * Find all accounts by user ID.
     *
     * @param userId The ID of the user.
     * @return A list of accounts associated with the user ID.
     */
    List<Account> findAccountsByUserId(Long userId);

    /**
     * Find an account by user ID and account ID.
     *
     * @param userId    The ID of the user.
     * @param accountId The ID of the account.
     * @return An Optional containing the account found, or empty if not found.
     */
    Optional<Account> findAccountByUserIdAndAccountId(Long userId, Long accountId);

    /**
     * Find an account by its ID.
     *
     * @param id The ID of the account.
     * @return An Optional containing the account found, or empty if not found.
     */
    Optional<Account> findAccountById(Long id);

    /**
     * Save an account.
     *
     * @param account The account to save.
     * @return The saved account.
     */
    Account save(Account account);
}
