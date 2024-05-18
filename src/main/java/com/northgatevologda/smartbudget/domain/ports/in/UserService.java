package com.northgatevologda.smartbudget.domain.ports.in;

import com.northgatevologda.smartbudget.application.service.refreshtoken.dto.AuthenticatedUserDTO;
import com.northgatevologda.smartbudget.application.service.user.dto.UserAuthenticationDTO;
import com.northgatevologda.smartbudget.application.service.user.dto.UserRegistrationDTO;
import com.northgatevologda.smartbudget.domain.model.User;

/**
 * Service for user entity
 */
public interface UserService {

    /**
     * Sign in a user using the provided authentication details.
     *
     * @param userAuthenticationDTO the DTO containing user authentication information
     * @return an AuthenticatedUserDTO representing the authenticated user
     */
    AuthenticatedUserDTO signIn(UserAuthenticationDTO userAuthenticationDTO);

    /**
     * Sign up a new user using the provided registration details.
     *
     * @param userRegistrationDTO the DTO containing user registration information
     */
    void signup(UserRegistrationDTO userRegistrationDTO);

    /**
     * Renew the access token using the provided refresh token.
     *
     * @param refreshToken the refresh token used for token renewal
     * @return the renewed access token
     */
    String renewAccessToken(String refreshToken);

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
    User findByUsername(String username);

    /**
     * Find a user by the id
     *
     * @param id the id of the user to search for
     * @return an Optional containing the User, if found
     */
    User findById(Long id);

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
