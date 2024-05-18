package com.northgatevologda.smartbudget.domain.ports.in;

import com.northgatevologda.smartbudget.domain.model.RefreshToken;
import com.northgatevologda.smartbudget.domain.model.User;

/**
 * Interface for handling refresh tokens.
 */
public interface RefreshTokenService {

    /**
     * Delete the refresh token associated with the user ID.
     *
     * @param userId The ID of the user for whom to delete the refresh token.
     */
    void deleteRefreshTokenByUserId(Long userId);

    /**
     * Find a refresh token by its token value.
     *
     * @param token The token value of the refresh token to find.
     * @return The refresh token found, or null if not found.
     */
    RefreshToken findByToken(String token);

    /**
     * Create a new refresh token for the specified user.
     *
     * @param user The user for whom to create the refresh token.
     * @return The created refresh token.
     */
    RefreshToken createRefreshToken(User user);

    /**
     * Verify if the refresh token has expired.
     *
     * @param token The refresh token to verify.
     * @return The verified refresh token.
     */
    RefreshToken verifyExpiration(RefreshToken token);
}
