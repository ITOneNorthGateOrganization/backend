package com.northgatevologda.smartbudget.application.service.transaction;

import com.northgatevologda.smartbudget.application.service.transaction.dto.TransactionDTO;
import com.northgatevologda.smartbudget.application.service.transaction.mapper.TransactionServiceMapper;
import com.northgatevologda.smartbudget.domain.enums.ETransactionType;
import com.northgatevologda.smartbudget.domain.exception.BadRequestException;
import com.northgatevologda.smartbudget.domain.exception.ForbiddenException;
import com.northgatevologda.smartbudget.domain.exception.NotFoundException;
import com.northgatevologda.smartbudget.domain.model.*;
import com.northgatevologda.smartbudget.domain.ports.in.*;
import com.northgatevologda.smartbudget.domain.ports.out.TransactionRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Implementation of {@link TransactionService}
 */
@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionServiceMapper transactionServiceMapper;
    private final TransactionRepositoryPort transactionRepositoryPort;
    private final AccountService accountService;
    private final UserService userService;
    private final TransactionTypeService transactionTypeService;
    private final BudgetService budgetService;

    public TransactionServiceImpl(
            TransactionServiceMapper transactionServiceMapper,
            @Lazy BudgetService budgetService,
            UserService userService,
            TransactionTypeService transactionTypeService,
            AccountService accountService,
            TransactionRepositoryPort transactionRepositoryPort
    ) {
        this.transactionServiceMapper = transactionServiceMapper;
        this.budgetService = budgetService;
        this.userService = userService;
        this.transactionTypeService = transactionTypeService;
        this.accountService = accountService;
        this.transactionRepositoryPort = transactionRepositoryPort;
    }

    @Override
    public Transaction save(String username, TransactionDTO transactionDTO) {
        logger.info("Creating transaction for user with username: {}", username);
        User foundUser = userService.findByUsername(username);
        validateTransactionAccounts(transactionDTO);

        TransactionType transactionType = transactionTypeService.findById(transactionDTO.getTransactionTypeId());
        Transaction transactionForCreate = prepareTransaction(transactionDTO, transactionType);

        processTransactionByType(foundUser, transactionForCreate);

        transactionForCreate.setCreatedAt(Instant.now());
        return createTransaction(transactionForCreate);
    }

    private void validateTransactionAccounts(TransactionDTO transactionDTO) {
        if (transactionDTO.getReceiverId() != null) {
            checkAccountById(transactionDTO.getReceiverId());
        }
        if (transactionDTO.getSenderId() != null) {
            checkAccountById(transactionDTO.getSenderId());
        }
    }

    private Transaction prepareTransaction(TransactionDTO transactionDTO, TransactionType transactionType) {
        Transaction transaction = transactionServiceMapper.toEntity(transactionDTO);
        transaction.setTransactionType(transactionType);
        return transaction;
    }

    private void processTransactionByType(User foundUser, Transaction transaction) {
        Long receiverAccountId = transaction.getReceiverId();
        Long senderAccountId = transaction.getSenderId();
        TransactionType transactionType = transaction.getTransactionType();
        if (transactionType.getId() == ETransactionType.REPLENISHMENT.getId()) {
            Account receiverAccount = accountService
                    .findAccountByUserIdAndAccountId(foundUser.getId(), receiverAccountId);
            processReplenishmentTransaction(transaction, receiverAccount);
        } else if (transactionType.getId() == ETransactionType.EXPENDITURE.getId()) {
            Account senderAccount = accountService
                    .findAccountByUserIdAndAccountId(foundUser.getId(), senderAccountId);
            processExpenditureTransaction(transaction, senderAccount);
        } else if (transactionType.getId() == ETransactionType.TRANSLATION.getId()) {
            Account senderAccount = accountService
                    .findAccountByUserIdAndAccountId(foundUser.getId(), senderAccountId);
            Account receiverAccount = accountService.findAccountById(receiverAccountId);
            processTranslationTransaction(transaction, senderAccount, receiverAccount);
        } else {
            throw new BadRequestException("An unknown transaction type has been transmitted");
        }
    }

    private Transaction createTransaction(Transaction transaction) {
        Transaction createdTransaction = transactionRepositoryPort.save(transaction);
        logger.info("Transaction created successfully");
        return createdTransaction;
    }

    private void checkAccountById(Long accountId) {
        Account account = accountService.findAccountById(accountId);
        if (!account.isOpen()) {
            throw new BadRequestException("Account with id " + accountId + " is not open");
        }
    }

    private void processReplenishmentTransaction(Transaction transaction, Account receiverAccount) {
        BigDecimal receiverBalance = receiverAccount.getBalance().add(transaction.getAmount());
        transaction.setReceiverBalance(receiverBalance);
        transaction.setAccount(receiverAccount);
        receiverAccount.setBalance(receiverBalance);
        accountService.update(receiverAccount);
    }

    private void processExpenditureTransaction(Transaction transaction, Account senderAccount) {
        if (senderAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new BadRequestException("You cannot create a transaction whose amount exceeds the account balance");
        }
        BigDecimal senderBalance = senderAccount.getBalance().subtract(transaction.getAmount());
        transaction.setSenderBalance(senderBalance);
        transaction.setAccount(senderAccount);
        senderAccount.setBalance(senderBalance);
        accountService.update(senderAccount);
    }

    private void processTranslationTransaction(
            Transaction transaction,
            Account senderAccount,
            Account receiverAccount
    ) {
        if (senderAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new BadRequestException("You cannot create a transaction whose amount exceeds the account balance");
        }
        BigDecimal senderBalance = senderAccount.getBalance().subtract(transaction.getAmount());
        BigDecimal receiverBalance = receiverAccount.getBalance().add(transaction.getAmount());
        senderAccount.setBalance(senderBalance);
        receiverAccount.setBalance(receiverBalance);
        transaction.setSenderBalance(senderBalance);
        transaction.setReceiverBalance(receiverBalance);
        transaction.setAccount(senderAccount);
        accountService.update(senderAccount);
        accountService.update(receiverAccount);
    }

    @Override
    public Transaction findTransactionById(Long id) {
        logger.info("Finding transaction with ID {}", id);
        return transactionRepositoryPort.findTransactionById(id)
                .orElseThrow(() -> new NotFoundException("Transaction was not found by id " + id));
    }

    @Override
    public List<Transaction> findTransactionsByUsername(String username) {
        logger.info("Retrieving transactions for user with username: {}", username);
        User user = userService.findByUsername(username);
        return transactionRepositoryPort.findTransactionsByUserId(user.getId());
    }

    @Override
    public List<Transaction> findTransactionsByUsernameAndAccountId(String username, Long accountId) {
        logger.info("Retrieving transactions for account ID: {} for user with username: {}", accountId, username);
        User user = userService.findByUsername(username);
        Account account = accountService.findAccountById(accountId);
        if (!account.getUser().getUsername().equals(username)) {
            throw new ForbiddenException("You cannot view transactions on someone else's account");
        }
        return transactionRepositoryPort.findTransactionsByUserIdAndAccountId(user.getId(), accountId);
    }

    @Override
    public List<Transaction> findUnaccountedTransactionsByUsername(String username) {
        logger.info("Retrieving unaccounted transactions for user with username: {}", username);
        User user = userService.findByUsername(username);
        return transactionRepositoryPort
                .findUnaccountedTransactionsByUserId(user.getId());
    }

    @Override
    public List<Transaction> findTransactionsByUsernameAndBudgetId(String username, Long budgetId) {
        logger.info("Retrieving transactions for budget ID: {} for user with username: {}", budgetId, username);
        User user = userService.findByUsername(username);
        Budget budget = budgetService.findBudgetById(budgetId);
        if (!budget.getUser().getUsername().equals(username)) {
            throw new ForbiddenException("You cannot view transactions on someone else's budget");
        }
        return transactionRepositoryPort.findTransactionsByUserIdAndBudgetId(user.getId(), budgetId);
    }

    @Override
    public void changeDescription(String username, Long transactionId, String newDescription) {
        logger.info("Changing description for transaction ID: {}", transactionId);
        Transaction transaction = findTransactionById(transactionId);
        Account accountFromTransaction = transaction.getAccount();
        if (!accountFromTransaction.getUser().getUsername().equals(username)) {
            throw new ForbiddenException("It is forbidden to update the transaction description of another user");
        }
        transactionRepositoryPort.changeDescription(transactionId, newDescription);
        logger.info("Description updated successfully");
    }
}
