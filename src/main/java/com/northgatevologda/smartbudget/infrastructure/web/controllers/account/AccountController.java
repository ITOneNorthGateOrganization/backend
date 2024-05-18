package com.northgatevologda.smartbudget.infrastructure.web.controllers.account;

import com.northgatevologda.smartbudget.application.service.account.dto.AccountDTO;
import com.northgatevologda.smartbudget.application.service.account.mapper.AccountServiceMapper;
import com.northgatevologda.smartbudget.domain.model.Account;
import com.northgatevologda.smartbudget.domain.ports.in.AccountService;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.account.dto.AccountCreateRequest;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.account.mapper.AccountControllerMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountControllerMapper accountControllerMapper;
    private final AccountServiceMapper accountServiceMapper;
    private final AccountService accountService;

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDTO>> findAccounts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Retrieving accounts for user with username: {}", username);
        List<Account> foundAccounts = accountService.findAccounts(username);
        return ResponseEntity.ok(accountServiceMapper.toDTOList(foundAccounts));
    }

    @PostMapping("/accounts")
    public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody AccountCreateRequest accountCreateRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Creating account for user with username: {}", username);
        AccountDTO accountForCreate = accountControllerMapper.toAccountDTO(accountCreateRequest);
        Account createdAccount = accountService.save(username, accountForCreate);
        return ResponseEntity.ok(accountServiceMapper.toDTO(createdAccount));
    }

    @PutMapping("/accounts/{accountId}/close")
    public ResponseEntity<String> closeAccounts(@PathVariable Long accountId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Closing account with ID: {} for user with username: {}", accountId, username);
        accountService.closeAccount(username, accountId);
        return ResponseEntity.ok("The account was successfully closed");
    }
}
