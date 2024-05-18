package com.northgatevologda.smartbudget.infrastructure.jpa.account;

import com.northgatevologda.smartbudget.domain.model.Account;
import com.northgatevologda.smartbudget.domain.ports.out.AccountRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JpaAccountRepositoryAdapter implements AccountRepositoryPort {
    private static final Logger logger = LoggerFactory.getLogger(JpaAccountRepositoryAdapter.class);

    private final JpaAccountRepository jpaAccountRepository;

    @Override
    public List<Account> findAccountsByUserId(Long userId) {
        logger.info("Finding accounts by user id: {}", userId);
        return jpaAccountRepository.findAccountsByUserId(userId);
    }

    @Override
    public Optional<Account> findAccountByUserIdAndAccountId(Long userId, Long accountId) {
        logger.info("Finding account by user ID {} and account ID {}", userId, accountId);
        return jpaAccountRepository.findAccountByUserIdAndId(userId, accountId);
    }

    @Override
    public Optional<Account> findAccountById(Long id) {
        logger.info("Getting Account by id: {}", id);
        return jpaAccountRepository.findById(id);
    }

    @Override
    public Account save(Account account) {
        logger.info("Creating Account: {}", account);
        return jpaAccountRepository.save(account);
    }
}
