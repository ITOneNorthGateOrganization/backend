package com.northgatevologda.smartbudget.infrastructure.jpa.account;

import com.northgatevologda.smartbudget.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaAccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAccountsByUserId(Long userId);

    Optional<Account> findAccountByUserIdAndId(Long userId, Long id);
}
