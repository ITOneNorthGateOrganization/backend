package com.northgatevologda.smartbudget.infrastructure.web.controllers.transaction;

import com.northgatevologda.smartbudget.application.service.transaction.dto.TransactionDTO;
import com.northgatevologda.smartbudget.application.service.transaction.mapper.TransactionServiceMapper;
import com.northgatevologda.smartbudget.domain.model.Transaction;
import com.northgatevologda.smartbudget.domain.ports.in.TransactionService;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.transaction.dto.ChangeDescriptionTransactionRequest;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.transaction.dto.CreateTransactionRequest;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.transaction.mapper.TransactionControllerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Transaction Controller", description = "Controller for managing user transactions")
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    private final TransactionService transactionService;
    private final TransactionControllerMapper transactionControllerMapper;
    private final TransactionServiceMapper transactionServiceMapper;

    @PostMapping(value = "/accounts/{accountId}/transactions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a transaction for a specific account",
            description = "Creates and records a new financial transaction under a specific account for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transaction created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid transaction data"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Account not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<TransactionDTO> createTransaction(
            @Parameter(description = "Account ID for which the transaction is to be created", required = true)
            @PathVariable Long accountId,
            @Parameter(description = "Transaction creation request data", required = true)
            @Valid @RequestBody CreateTransactionRequest createTransactionRequest
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Creating transaction for user with username: {}, account ID: {}", username, accountId);
        TransactionDTO transactionDTO = transactionControllerMapper.toTransactionDTO(createTransactionRequest);
        Transaction createdTransaction = transactionService.save(username, accountId, transactionDTO);
        logger.info("Transaction created successfully");
        return ResponseEntity.ok(transactionServiceMapper.toDTO(createdTransaction));
    }

    @GetMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve all transactions for the authenticated user",
            description = "Fetches all financial transactions associated with the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<TransactionDTO>> findTransactionByUsername() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Retrieving transactions for user with username: {}", username);
        List<Transaction> foundTransactions = transactionService.findTransactionsByUsername(username);
        return ResponseEntity.ok(transactionServiceMapper.toDTOList(foundTransactions));
    }

    @GetMapping(value = "/accounts/{accountId}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve transactions by account ID",
            description = "Fetches all transactions made under a specified account for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Account not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<TransactionDTO>> findTransactionByAccountId(
            @Parameter(description = "The ID of the account to fetch transactions from", required = true)
            @PathVariable Long accountId
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Retrieving transactions for account ID: {} for user with username: {}", accountId, username);
        List<Transaction> foundTransactions =
                transactionService.findTransactionsByUsernameAndAccountId(username, accountId);
        return ResponseEntity.ok(transactionServiceMapper.toDTOList(foundTransactions));
    }

    @GetMapping(value = "/unaccountedTransactions", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve unaccounted transactions",
            description = "Fetches all transactions that have not been accounted for under any specific category for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Unaccounted transactions retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<TransactionDTO>> findUnaccountedTransactions() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Retrieving unaccounted transactions for user with username: {}", username);
        List<Transaction> foundTransactions =
                transactionService.findUnaccountedTransactionsByUsername(username);
        return ResponseEntity.ok(transactionServiceMapper.toDTOList(foundTransactions));
    }

    @PutMapping(value = "/transactions/{transactionId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update transaction description",
            description = "Updates the description of a specific transaction for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The transaction description has been successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Transaction not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid data provided"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<String> changeDescriptionByTransactionId(
            @Parameter(description = "Transaction ID whose description needs updating", required = true)
            @PathVariable Long transactionId,
            @Parameter(description = "Request body containing the new description", required = true)
            @Valid @RequestBody ChangeDescriptionTransactionRequest changeDescriptionTransactionRequest
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Updating description for transaction ID: {} for user with username: {}", transactionId, username);
        transactionService
                .changeDescription(username, transactionId, changeDescriptionTransactionRequest.getDescription());
        logger.info("Description updated successfully");
        return ResponseEntity.ok("The transaction description has been successfully updated");
    }

    @GetMapping(value = "/budgets/{budgetId}/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve transactions by budget ID",
            description = "Fetches all transactions allocated to a specific budget for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Budget not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<TransactionDTO>> findTransactionsByBudgetId(
            @Parameter(description = "Budget ID whose transactions are to be retrieved", required = true)
            @PathVariable Long budgetId
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Retrieving transactions for budget ID: {} for user with username: {}", budgetId, username);
        List<Transaction> foundTransactions =
                transactionService.findTransactionsByUsernameAndBudgetId(username, budgetId);
        return ResponseEntity.ok(transactionServiceMapper.toDTOList(foundTransactions));
    }
}
