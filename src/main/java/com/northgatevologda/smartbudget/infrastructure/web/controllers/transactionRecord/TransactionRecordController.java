package com.northgatevologda.smartbudget.infrastructure.web.controllers.transactionrecord;

import com.northgatevologda.smartbudget.application.service.transactionrecord.dto.TransactionRecordDTO;
import com.northgatevologda.smartbudget.application.service.transactionrecord.mapper.TransactionRecordServiceMapper;
import com.northgatevologda.smartbudget.domain.model.TransactionRecord;
import com.northgatevologda.smartbudget.domain.ports.in.TransactionRecordService;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.transactionrecord.dto.CreateTransactionRecordRequest;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.transactionrecord.dto.UpdateTransactionRecordRequest;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.transactionrecord.mapper.TransactionRecordControllerMapper;
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
public class TransactionRecordController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionRecordController.class);

    private final TransactionRecordControllerMapper transactionRecordControllerMapper;
    private final TransactionRecordServiceMapper recordServiceMapper;
    private final TransactionRecordService transactionRecordService;
    private final TransactionRecordServiceMapper transactionRecordServiceMapper;

    @PostMapping("/transactions/{transactionId}/transactionRecords")
    public ResponseEntity<TransactionRecordDTO> create(
            @PathVariable Long transactionId,
            @Valid @RequestBody CreateTransactionRecordRequest createTransactionRecordRequest
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Creating transaction record for transaction ID: {} for user with username: {}",
                transactionId, username);
        TransactionRecordDTO transactionRecordDTO =
                transactionRecordControllerMapper.toTransactionRecordDTO(createTransactionRecordRequest);
        TransactionRecord createdTransactionRecord =
                transactionRecordService.save(username, transactionId, transactionRecordDTO);
        logger.info("Transaction record created successfully");
        return ResponseEntity.ok(recordServiceMapper.toDTO(createdTransactionRecord));
    }

    @GetMapping("/transactions/{transactionId}/transactionRecords")
    public ResponseEntity<List<TransactionRecordDTO>> findTransactionRecordsByTransactionId(
            @PathVariable Long transactionId
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Finding transaction records by transaction ID: {} for user with username: {}",
                transactionId, username);
        List<TransactionRecord> foundTransactionRecords =
                transactionRecordService.findByUsernameAndTransactionId(username, transactionId);
        return ResponseEntity.ok(recordServiceMapper.toDTOList(foundTransactionRecords));
    }

    @DeleteMapping("/transactions/transactionRecords/{transactionRecordId}")
    public ResponseEntity<String> deleteTransactionRecordById(
            @PathVariable Long transactionRecordId
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Deleting transaction record with ID: {} for user with username: {}", transactionRecordId, username);
        transactionRecordService.deleteTransactionRecordById(username, transactionRecordId);
        logger.info("Transaction record deleted successfully");
        return ResponseEntity.ok("The transaction record was successfully deleted");
    }

    @PutMapping("/transactions/transactionRecords/{transactionRecordId}")
    public ResponseEntity<TransactionRecordDTO> updateTransactionRecord(
            @PathVariable Long transactionRecordId,
            @Valid @RequestBody UpdateTransactionRecordRequest updateTransactionRecordRequest
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Updating transaction record with ID: {} for user with username: {}",
                transactionRecordId, username);
        TransactionRecordDTO transactionRecordDTO =
                transactionRecordControllerMapper.toTransactionRecordDTO(updateTransactionRecordRequest);
        TransactionRecord savedTransactionRecord =
                transactionRecordService.update(username, transactionRecordId, transactionRecordDTO);
        logger.info("Transaction record updated successfully");
        return ResponseEntity.ok(transactionRecordServiceMapper.toDTO(savedTransactionRecord));
    }
}
