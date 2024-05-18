package com.northgatevologda.smartbudget.infrastructure.web.controllers.transaction;

import com.northgatevologda.smartbudget.application.service.transaction.dto.TransactionDTO;
import com.northgatevologda.smartbudget.application.service.transaction.mapper.TransactionServiceMapper;
import com.northgatevologda.smartbudget.domain.model.Transaction;
import com.northgatevologda.smartbudget.domain.ports.in.TransactionService;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.transaction.dto.ChangeDescriptionTransactionRequest;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.transaction.dto.CreateTransactionRequest;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.transaction.mapper.TransactionControllerMapper;
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
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;
    private final TransactionControllerMapper transactionControllerMapper;
    private final TransactionServiceMapper transactionServiceMapper;

    @PostMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<TransactionDTO> createTransaction(
            @PathVariable Long accountId,
            @Valid @RequestBody CreateTransactionRequest createTransactionRequest
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Creating transaction for user with username: {}, account ID: {}", username, accountId);
        TransactionDTO transactionDTO = transactionControllerMapper.toTransactionDTO(createTransactionRequest);
        Transaction createdTransaction = transactionService.save(username, accountId, transactionDTO);
        logger.info("Transaction created successfully");
        return ResponseEntity.ok(transactionServiceMapper.toDTO(createdTransaction));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> findTransactionByUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Retrieving transactions for user with username: {}", username);
        List<Transaction> foundTransactions = transactionService.findTransactionsByUsername(username);
        return ResponseEntity.ok(transactionServiceMapper.toDTOList(foundTransactions));
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionDTO>> findTransactionByAccountId(
            @PathVariable Long accountId
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Retrieving transactions for account ID: {} for user with username: {}", accountId, username);
        List<Transaction> foundTransactions =
                transactionService.findTransactionsByUsernameAndAccountId(username, accountId);
        return ResponseEntity.ok(transactionServiceMapper.toDTOList(foundTransactions));
    }

    @GetMapping("/unaccountedTransactions")
    public ResponseEntity<List<TransactionDTO>> findUnaccountedTransactions() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Retrieving unaccounted transactions for user with username: {}", username);
        List<Transaction> foundTransactions =
                transactionService.findUnaccountedTransactionsByUsername(username);
        return ResponseEntity.ok(transactionServiceMapper.toDTOList(foundTransactions));
    }

    @PutMapping("/transactions/{transactionId}")
    public ResponseEntity<String> changeDescriptionByTransactionId(
            @PathVariable Long transactionId,
            @Valid @RequestBody ChangeDescriptionTransactionRequest changeDescriptionTransactionRequest
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Updating description for transaction ID: {} for user with username: {}", transactionId, username);
        transactionService
                .changeDescription(username, transactionId, changeDescriptionTransactionRequest.getDescription());
        logger.info("Description updated successfully");
        return ResponseEntity.ok("The transaction description has been successfully updated");
    }

    @GetMapping("/budgets/{budgetId}/transactions")
    public ResponseEntity<List<TransactionDTO>> findTransactionsByBudgetId(@PathVariable Long budgetId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Retrieving transactions for budget ID: {} for user with username: {}", budgetId, username);
        List<Transaction> foundTransactions =
                transactionService.findTransactionsByUsernameAndBudgetId(username, budgetId);
        return ResponseEntity.ok(transactionServiceMapper.toDTOList(foundTransactions));
    }
}
