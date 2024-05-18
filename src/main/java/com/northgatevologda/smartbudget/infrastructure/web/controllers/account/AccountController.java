package com.northgatevologda.smartbudget.infrastructure.web.controllers.account;

import com.northgatevologda.smartbudget.application.service.account.dto.AccountDTO;
import com.northgatevologda.smartbudget.application.service.account.mapper.AccountServiceMapper;
import com.northgatevologda.smartbudget.domain.model.Account;
import com.northgatevologda.smartbudget.domain.ports.in.AccountService;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.account.dto.AccountCreateRequest;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.account.mapper.AccountControllerMapper;
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
@Tag(name = "Account Controller", description = "Controller for managing user accounts")
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final AccountControllerMapper accountControllerMapper;
    private final AccountServiceMapper accountServiceMapper;
    private final AccountService accountService;

    @Operation(summary = "Retrieve all accounts associated with the currently authenticated user",
            description = "This API returns a list of accounts that belong to the authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of accounts"),
                    @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
                    @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
                    @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
            })
    @GetMapping(value = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AccountDTO>> findAccounts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Retrieving accounts for user with username: {}", username);
        List<Account> foundAccounts = accountService.findAccounts(username);
        return ResponseEntity.ok(accountServiceMapper.toDTOList(foundAccounts));
    }

    @Operation(summary = "Create an account for the authenticated user",
            description = "This API is used to create a new account tied to the user's username who is currently authenticated",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully created the account"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                    @ApiResponse(responseCode = "401", description = "You are not authorized to perform this operation"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @PostMapping(value = "/accounts", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDTO> createAccount(
            @Parameter(description = "Request body for account creation", required = true)
            @Valid @RequestBody AccountCreateRequest accountCreateRequest
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Creating account for user with username: {}", username);
        AccountDTO accountForCreate = accountControllerMapper.toAccountDTO(accountCreateRequest);
        Account createdAccount = accountService.save(username, accountForCreate);
        return ResponseEntity.ok(accountServiceMapper.toDTO(createdAccount));
    }

    @Operation(summary = "Close a user's account",
            description = "Closes the account specified by account ID for the currently authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully closed the account"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized to access this resource"),
                    @ApiResponse(responseCode = "404", description = "Account not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @PutMapping(value = "/{accountId}/close", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> closeAccount(
            @Parameter(description = "ID of the account to be closed", required = true)
            @PathVariable Long accountId
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Closing account with ID: {} for user with username: {}", accountId, username);
        accountService.closeAccount(username, accountId);
        return ResponseEntity.ok("The account was successfully closed");
    }
}
