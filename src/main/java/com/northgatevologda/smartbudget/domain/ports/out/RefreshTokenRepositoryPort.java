package com.northgatevologda.smartbudget.domain.ports.out;

import com.northgatevologda.smartbudget.domain.model.RefreshToken;

import java.util.Optional;

/**
 * Interface for interacting with refresh token data in the application.
 */
public interface RefreshTokenRepositoryPort {

    /**
     * Deletes the refresh token associated with the specified userId.
     *
     * @param userId the unique identifier of the user whose refresh token should be deleted
     */
    void deleteRefreshTokenByUserId(Long userId);

    /**
     * Find a refresh token by its token.
     *
     * @param token the token value to search for
     * @return an Optional containing the RefreshToken, if found
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Save a refresh token.
     *
     * @param refreshToken the RefreshToken to save
     * @return the saved RefreshToken
     */
    RefreshToken save(RefreshToken refreshToken);

    /**
     * Delete a refresh token.
     *
     * @param refreshTokenId the RefreshToken id to delete
     */
    void delete(Long refreshTokenId);
}
