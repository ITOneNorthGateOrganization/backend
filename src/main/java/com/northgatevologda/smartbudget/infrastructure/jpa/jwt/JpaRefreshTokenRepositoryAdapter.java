package com.northgatevologda.smartbudget.infrastructure.jpa.jwt;

import com.northgatevologda.smartbudget.domain.model.RefreshToken;
import com.northgatevologda.smartbudget.domain.ports.out.RefreshTokenRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of {@link RefreshTokenRepositoryPort}
 */
@Component
@RequiredArgsConstructor
public class JpaRefreshTokenRepositoryAdapter implements RefreshTokenRepositoryPort {
    private static final Logger logger = LoggerFactory.getLogger(JpaRefreshTokenRepositoryAdapter.class);

    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;

    @Override
    public void deleteRefreshTokenByUserId(Long userId) {
        logger.info("Deleting refresh token for user with id : {}", userId);
        jpaRefreshTokenRepository.deleteRefreshTokenByUserId(userId);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        logger.info("Finding refresh token by token: {}", token);
        return jpaRefreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        logger.info("Saving refresh token: {}", refreshToken);
        return jpaRefreshTokenRepository.save(refreshToken);
    }

    @Override
    public void delete(Long refreshTokenId) {
        logger.info("Deleting refresh token by id: {}", refreshTokenId);
        jpaRefreshTokenRepository.deleteById(refreshTokenId);
    }
}
