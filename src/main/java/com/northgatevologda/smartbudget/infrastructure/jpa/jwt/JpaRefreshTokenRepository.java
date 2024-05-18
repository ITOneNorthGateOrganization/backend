package com.northgatevologda.smartbudget.infrastructure.jpa.jwt;

import com.northgatevologda.smartbudget.domain.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface JpaRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Transactional
    void deleteRefreshTokenByUserId(Long userId);
}
