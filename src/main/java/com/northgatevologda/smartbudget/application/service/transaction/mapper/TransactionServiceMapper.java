package com.northgatevologda.smartbudget.application.service.transaction.mapper;

import com.northgatevologda.smartbudget.application.service.transaction.dto.TransactionDTO;
import com.northgatevologda.smartbudget.domain.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Class responsible for mapping TransactionDTO objects to Transaction objects.
 */
@RequiredArgsConstructor
@Component
public class TransactionServiceMapper {

    /**
     * Maps a list of Transaction objects to a list of TransactionDTO objects.
     *
     * @param transactions The list of Transaction objects to be mapped
     * @return List of TransactionDTO objects with data from the Transaction objects
     */
    public List<TransactionDTO> toDTOList(List<Transaction> transactions) {
        return transactions.stream().map(this::toDTO).toList();
    }

    /**
     * Maps a Transaction object to a TransactionDTO object.
     *
     * @param transaction The Transaction object to be mapped
     * @return TransactionDTO object with data from the Transaction object
     */
    public TransactionDTO toDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .description(transaction.getDescription())
                .amount(transaction.getAmount())
                .createdAt(transaction.getCreatedAt())
                .senderId(transaction.getSenderId())
                .receiverId(transaction.getReceiverId())
                .transactionTypeId(transaction.getTransactionType().getId())
                .build();
    }

    /**
     * Maps a TransactionDTO object to a Transaction object.
     *
     * @param transactionDTO The TransactionDTO object to be mapped
     * @return Transaction object with data from the TransactionDTO object
     */
    public Transaction toEntity(TransactionDTO transactionDTO) {
        return Transaction.builder()
                .id(transactionDTO.getId())
                .description(transactionDTO.getDescription())
                .amount(transactionDTO.getAmount())
                .createdAt(transactionDTO.getCreatedAt())
                .senderId(transactionDTO.getSenderId())
                .receiverId(transactionDTO.getReceiverId())
                .build();
    }
}
