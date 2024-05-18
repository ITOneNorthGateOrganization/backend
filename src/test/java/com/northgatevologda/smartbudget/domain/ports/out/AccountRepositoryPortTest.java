package com.northgatevologda.smartbudget.domain.ports.out;

import com.northgatevologda.smartbudget.domain.enums.ERole;
import com.northgatevologda.smartbudget.domain.model.Account;
import com.northgatevologda.smartbudget.domain.model.Category;
import com.northgatevologda.smartbudget.domain.model.Role;
import com.northgatevologda.smartbudget.domain.model.User;
import com.northgatevologda.smartbudget.infrastructure.jpa.account.JpaAccountRepository;
import com.northgatevologda.smartbudget.infrastructure.jpa.account.JpaAccountRepositoryAdapter;
import com.northgatevologda.smartbudget.infrastructure.jpa.role.JpaRoleRepository;
import com.northgatevologda.smartbudget.infrastructure.jpa.user.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest()
@ActiveProfiles("test")
class AccountRepositoryPortTest {
    @Autowired
    private JpaAccountRepositoryAdapter jpaAccountRepositoryAdapter;

    @Autowired
    private JpaAccountRepository jpaAccountRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    protected JpaRoleRepository jpaRoleRepository;

    private User accountOwner;
    private Account account;

    @BeforeEach
    void setUp() {
        createAccountOwner();
        createAccount();
    }

    private void createAccountOwner() {
        Set<Role> roles = new HashSet<>();
        Role userRole = Role.builder().name(ERole.ROLE_USER).build();
        roles.add(userRole);
        jpaRoleRepository.saveAll(roles);

        accountOwner = User.builder()
                .username("Test user")
                .email("test@mail.ru")
                .password("1111")
                .createdAt(Instant.now())
                .roles(roles)
                .categories(new ArrayList<>())
                .build();

        jpaUserRepository.save(accountOwner);
    }

    private void createAccount() {
        account = Account.builder()
                .name("Test account")
                .balance(new BigDecimal("5000"))
                .open(true)
                .updateAt(Instant.now())
                .user(accountOwner)
                .build();

        jpaAccountRepository.save(account);
    }

    @Test
    void givenExistingAccountId_WhenFindAccountById_ThenReturnCorrectAccount() {
        Long accountId = account.getId();
        Optional<Account> optionalAccount = jpaAccountRepositoryAdapter.findAccountById(accountId);
        assertTrue(optionalAccount.isPresent());
        assertEquals(account, optionalAccount.get());
    }

    @Test
    void givenNotExistingAccountId_WhenFindAccountById_ThenReturnOptionalEmpty() {
        Long accountId = -1L;
        Optional<Account> optionalAccount = jpaAccountRepositoryAdapter.findAccountById(accountId);
        assertTrue(optionalAccount.isEmpty());
    }
}