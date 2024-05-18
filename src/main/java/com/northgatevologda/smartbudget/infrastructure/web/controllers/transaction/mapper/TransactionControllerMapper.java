package com.northgatevologda.smartbudget.infrastructure.web.controllers.transaction.mapper;

import com.northgatevologda.smartbudget.application.service.transaction.dto.TransactionDTO;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.transaction.dto.CreateTransactionRequest;
import org.springframework.stereotype.Component;

/**
 * Component responsible for mapping CreateTransactionRequest objects to TransactionDTO objects.
 */
@Component
public class TransactionControllerMapper {

    /**
     * Maps a CreateTransactionRequest object to a TransactionDTO object.
     *
     * @param createTransactionRequest The CreateTransactionRequest object to be mapped
     * @return TransactionDTO object with data from the CreateTransactionRequest object
     */
    public TransactionDTO toTransactionDTO(CreateTransactionRequest createTransactionRequest) {
        return TransactionDTO.builder()
                .description(createTransactionRequest.getDescription())
                .amount(createTransactionRequest.getAmount())
                .receiverId(createTransactionRequest.getReceiverId())
                .senderId(createTransactionRequest.getSenderId())
                .transactionTypeId(createTransactionRequest.getTransactionTypeId())
                .build();
    }
}
