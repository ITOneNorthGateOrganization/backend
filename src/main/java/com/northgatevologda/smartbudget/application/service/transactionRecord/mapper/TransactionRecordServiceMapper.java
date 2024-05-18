package com.northgatevologda.smartbudget.application.service.transactionrecord.mapper;

import com.northgatevologda.smartbudget.application.service.transactionrecord.dto.TransactionRecordDTO;
import com.northgatevologda.smartbudget.domain.model.TransactionRecord;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Component responsible for mapping TransactionRecord DTOs to entities and vice versa.
 */
@Component
public class TransactionRecordServiceMapper {

    /**
     * Maps a TransactionRecordDTO object to a TransactionRecord entity.
     *
     * @param transactionRecordDTO The TransactionRecordDTO object to be mapped
     * @return TransactionRecord entity with data from the TransactionRecordDTO object
     */
    public TransactionRecord toEntity(TransactionRecordDTO transactionRecordDTO) {
        return TransactionRecord.builder()
                .id(transactionRecordDTO.getId())
                .amount(transactionRecordDTO.getAmount())
                .description(transactionRecordDTO.getDescription())
                .build();
    }

    /**
     * Maps a TransactionRecord entity to a TransactionRecordDTO object.
     *
     * @param transactionRecord The TransactionRecord entity to be mapped
     * @return TransactionRecordDTO object with data from the TransactionRecord entity
     */
    public TransactionRecordDTO toDTO(TransactionRecord transactionRecord) {
        return TransactionRecordDTO.builder()
                .id(transactionRecord.getId())
                .amount(transactionRecord.getAmount())
                .budgetId(transactionRecord.getBudget().getId())
                .categoryId(transactionRecord.getCategory().getId())
                .description(transactionRecord.getDescription())
                .build();
    }

    /**
     * Maps a list of TransactionRecord entities to a list of TransactionRecordDTO objects.
     *
     * @param transactionRecords The list of TransactionRecord entities to be mapped
     * @return List of TransactionRecordDTO objects with data from the TransactionRecord entities
     */
    public List<TransactionRecordDTO> toDTOList(List<TransactionRecord> transactionRecords) {
        return transactionRecords.stream().map(this::toDTO).toList();
    }
}
