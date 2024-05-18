package com.northgatevologda.smartbudget.application.service.userdetails;

import com.northgatevologda.smartbudget.domain.exception.NotFoundException;
import com.northgatevologda.smartbudget.domain.model.User;
import com.northgatevologda.smartbudget.domain.ports.out.UserRepositoryPort;
import com.northgatevologda.smartbudget.infrastructure.web.entities.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link UserDetailsService}
 */
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UserDetails loadUserByUsername(String username) throws NotFoundException {
        logger.info("Loading user details by username: {}", username);
        User foundUser = userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> {
                    String errorMessage = "User not found with username: " + username;
                    logger.error(errorMessage);
                    return new NotFoundException(errorMessage);
                });
        return UserDetailsImpl.build(foundUser);
    }
}
