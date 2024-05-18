package com.northgatevologda.smartbudget.infrastructure.jpa.user;

import com.northgatevologda.smartbudget.domain.model.User;
import com.northgatevologda.smartbudget.domain.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of {@link UserRepositoryPort}
 */
@RequiredArgsConstructor
@Component
public class JpaUserRepositoryAdapter implements UserRepositoryPort {
    private static final Logger logger = LoggerFactory.getLogger(JpaUserRepositoryAdapter.class);

    private final JpaUserRepository jpaUserRepository;

    @Override
    public void save(User user) {
        logger.info("Saving user: {}", user);
        jpaUserRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        logger.info("Finding user by username: {}", username);
        return jpaUserRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        logger.info("Finding user by id: {}", id);
        return jpaUserRepository.findById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        logger.info("Checking if user exists by username: {}", username);
        return jpaUserRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        logger.info("Checking if user exists by email: {}", email);
        return jpaUserRepository.existsByEmail(email);
    }
}
