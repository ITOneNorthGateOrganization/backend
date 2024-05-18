package com.northgatevologda.smartbudget.domain.ports.out;

import com.northgatevologda.smartbudget.domain.model.User;

import java.util.Optional;

/**
 * Interface for interacting with user data in the application.
 */
public interface UserRepositoryPort {

    /**
     * Save a user.
     *
     * @param user the User to save
     */
    void save(User user);

    /**
     * Find a user by their username.
     *
     * @param username the username of the user to search for
     * @return an Optional containing the User, if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by the id
     *
     * @param id the id of the user to search for
     * @return an Optional containing the User, if found
     */
    Optional<User> findById(Long id);

    /**
     * Check if a user with the given username exists.
     *
     * @param username the username to check
     * @return true if a user with the username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Check if a user with the given email exists.
     *
     * @param email the email to check
     * @return true if a user with the email exists, false otherwise
     */
    boolean existsByEmail(String email);
}
