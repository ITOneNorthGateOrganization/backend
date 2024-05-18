package com.northgatevologda.smartbudget.application.service.refreshtoken;

import com.northgatevologda.smartbudget.domain.exception.NotFoundException;
import com.northgatevologda.smartbudget.domain.exception.TokenRefreshException;
import com.northgatevologda.smartbudget.domain.model.RefreshToken;
import com.northgatevologda.smartbudget.domain.model.User;
import com.northgatevologda.smartbudget.domain.ports.in.RefreshTokenService;
import com.northgatevologda.smartbudget.domain.ports.out.RefreshTokenRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

/**
 * Implementation of {@link RefreshTokenService}
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);

    private final RefreshTokenRepositoryPort refreshTokenRepositoryPort;

    @Value("${smartbudget.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Override
    public void deleteRefreshTokenByUserId(Long userId) {
        logger.info("Deleting refresh token by user id: {}", userId);
        refreshTokenRepositoryPort.deleteRefreshTokenByUserId(userId);
    }

    public RefreshToken findByToken(String token) {
        logger.info("Finding refresh token by token: {}", token);
        return refreshTokenRepositoryPort.findByToken(token)
                .orElseThrow(() -> new NotFoundException("the refresh token was not found"));
    }

    public RefreshToken createRefreshToken(User user) {
        logger.info("Creating refresh token for user: {}", user);
        Instant newExpiryDate = Instant.now().plusMillis(refreshTokenDurationMs);
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiryDate(newExpiryDate)
                .user(user)
                .build();
        return refreshTokenRepositoryPort.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        logger.info("Verifying expiration of refresh token: {}", token.getToken());
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            String message = "Refresh token was expired. Please make a new signin request";
            logger.error(message);
            refreshTokenRepositoryPort.delete(token.getId());
            throw new TokenRefreshException(token.getToken(), message);
        }
        return token;
    }
}
