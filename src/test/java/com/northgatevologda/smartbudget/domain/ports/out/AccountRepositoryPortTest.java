package com.northgatevologda.smartbudget.domain.ports.out;

import com.northgatevologda.smartbudget.domain.enums.ERole;
import com.northgatevologda.smartbudget.domain.model.Account;
import com.northgatevologda.smartbudget.domain.model.Role;
import com.northgatevologda.smartbudget.domain.model.User;
import com.northgatevologda.smartbudget.infrastructure.jpa.account.JpaAccountRepository;
import com.northgatevologda.smartbudget.infrastructure.jpa.role.JpaRoleRepository;
import com.northgatevologda.smartbudget.infrastructure.jpa.user.JpaUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@ActiveProfiles("test")
class AccountRepositoryPortTest {
    @Autowired
    private AccountRepositoryPort accountRepositoryPort;

    @Autowired
    private JpaAccountRepository jpaAccountRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaRoleRepository jpaRoleRepository;

    @AfterEach
    void tearDown() {
        dropAll();
    }

    private void dropAll() {
        jpaRoleRepository.deleteAll();
        jpaAccountRepository.deleteAll();
        jpaUserRepository.deleteAll();
    }

    @Test
    void givenExistingAccountId_WhenFindAccountById_ThenReturnCorrectAccount() {
        // Given
        User accountOwner = buildMockUser();
        Account account = buildMockAccount(accountOwner);
        Long accountId = account.getId();

        // When
        Optional<Account> optionalAccount = accountRepositoryPort.findAccountById(accountId);

        // Then
        assertTrue(optionalAccount.isPresent());
        assertEquals(accountId, optionalAccount.get().getId());
    }

    @Test
    void givenNotExistingAccountId_WhenFindAccountById_ThenReturnOptionalEmpty() {
        // Given
        Long accountId = -1L;

        // When
        Optional<Account> optionalAccount = accountRepositoryPort.findAccountById(accountId);

        // Then
        assertTrue(optionalAccount.isEmpty());
    }

    @Test
    void givenNewAccount_WhenSaveAccount_ThenAccountSaved() {
        // Given
        User accountOwner = buildMockUser();
        Account account = Account.builder()
                .name("Test account")
                .balance(new BigDecimal("5000"))
                .open(true)
                .updateAt(Instant.now())
                .user(accountOwner)
                .build();

        // When
        Account savedAccount = accountRepositoryPort.save(account);

        // Then
        checkAccounts(account, savedAccount);
    }

    private void checkAccounts(Account account1, Account account2) {
        assertEquals(account1.getName(), account2.getName());
        assertEquals(account1.getOpen(), account2.getOpen());
        assertEquals(account1.getUpdateAt(), account2.getUpdateAt());
        assertEquals(account1.getBalance(), account2.getBalance());
    }

    private User buildMockUser() {
        Set<Role> roles = new HashSet<>();
        Role userRole = Role.builder().name(ERole.ROLE_USER).build();
        roles.add(userRole);
        jpaRoleRepository.saveAll(roles);
        User user = User.builder()
                .username("Test user")
                .email("test@mail.ru")
                .password("1111")
                .createdAt(Instant.now())
                .roles(roles)
                .categories(new ArrayList<>())
                .build();
        jpaUserRepository.save(user);
        return user;
    }

    private Account buildMockAccount(User accountOwner) {
        Account account = Account.builder()
                .name("Test account")
                .balance(new BigDecimal("5000"))
                .open(true)
                .updateAt(Instant.now())
                .user(accountOwner)
                .build();
        jpaAccountRepository.save(account);
        return account;
    }
}