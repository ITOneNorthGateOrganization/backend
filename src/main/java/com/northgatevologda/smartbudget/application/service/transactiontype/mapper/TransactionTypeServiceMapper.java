package com.northgatevologda.smartbudget.application.service.transactiontype.mapper;

import com.northgatevologda.smartbudget.application.service.transactiontype.dto.TransactionTypeDTO;
import com.northgatevologda.smartbudget.domain.model.TransactionType;
import org.springframework.stereotype.Component;

/**
 * Class responsible for mapping TransactionType objects to TransactionTypeDTO objects.
 */
@Component
public class TransactionTypeServiceMapper {

    /**
     * Maps a TransactionType object to a TransactionTypeDTO object.
     *
     * @param transactionType The TransactionType object to be mapped
     * @return TransactionTypeDTO object with data from the TransactionType object
     */
    public TransactionTypeDTO toDTO(TransactionType transactionType) {
        return TransactionTypeDTO.builder()
                .id(transactionType.getId())
                .name(transactionType.getName())
                .build();
    }
}
