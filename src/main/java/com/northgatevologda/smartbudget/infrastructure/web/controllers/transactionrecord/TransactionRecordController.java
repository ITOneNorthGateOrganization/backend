package com.northgatevologda.smartbudget.infrastructure.web.controllers.transactionrecord;

import com.northgatevologda.smartbudget.application.service.transactionrecord.dto.TransactionRecordDTO;
import com.northgatevologda.smartbudget.application.service.transactionrecord.mapper.TransactionRecordServiceMapper;
import com.northgatevologda.smartbudget.domain.model.TransactionRecord;
import com.northgatevologda.smartbudget.domain.ports.in.TransactionRecordService;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.transactionrecord.dto.CreateTransactionRecordRequest;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.transactionrecord.dto.UpdateTransactionRecordRequest;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.transactionrecord.mapper.TransactionRecordControllerMapper;
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
@Tag(name = "Transaction Record Controller", description = "Controller for managing transaction records")
public class TransactionRecordController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionRecordController.class);

    private final TransactionRecordControllerMapper transactionRecordControllerMapper;
    private final TransactionRecordServiceMapper recordServiceMapper;
    private final TransactionRecordService transactionRecordService;
    private final TransactionRecordServiceMapper transactionRecordServiceMapper;

    @Operation(summary = "Create a transaction record",
            description = "Create a new transaction record for a specific transaction ID associated with the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transaction record created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid data provided"),
                    @ApiResponse(responseCode = "404", description = "Transaction ID not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @PostMapping(value = "/transactions/{transactionId}/transactionRecords", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionRecordDTO> create(
            @Parameter(description = "Transaction ID for which the record is to be created", required = true)
            @PathVariable Long transactionId,
            @Parameter(description = "Request body containing the new transaction record", required = true)
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

    @Operation(summary = "Find transaction records by transaction ID",
            description = "Retrieve all transaction records associated with a specified transaction ID for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transaction records retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Transaction ID not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @GetMapping(value = "/transactions/{transactionId}/transactionRecords", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TransactionRecordDTO>> findTransactionRecordsByTransactionId(
            @Parameter(description = "Transaction ID to fetch records from", required = true)
            @PathVariable Long transactionId
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Finding transaction records by transaction ID: {} for user with username: {}",
                transactionId, username);
        List<TransactionRecord> foundTransactionRecords =
                transactionRecordService.findByUsernameAndTransactionId(username, transactionId);
        return ResponseEntity.ok(recordServiceMapper.toDTOList(foundTransactionRecords));
    }

    @Operation(summary = "Delete transaction record by ID",
            description = "Deletes a specific transaction record based on the ID for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "The transaction record was successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Transaction record not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @DeleteMapping(value = "/transactions/transactionRecords/{transactionRecordId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteTransactionRecordById(
            @Parameter(description = "ID of the transaction record to be deleted", required = true)
            @PathVariable Long transactionRecordId
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Deleting transaction record with ID: {} for user with username: {}", transactionRecordId, username);
        transactionRecordService.deleteTransactionRecordById(username, transactionRecordId);
        logger.info("Transaction record deleted successfully");
        return ResponseEntity.ok("The transaction record was successfully deleted");
    }

    @Operation(summary = "Update transaction record",
            description = "Updates a specific transaction record based on the ID for the authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transaction record updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid data provided"),
                    @ApiResponse(responseCode = "404", description = "Transaction record not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    @PutMapping(value = "/transactions/transactionRecords/{transactionRecordId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TransactionRecordDTO> updateTransactionRecord(
            @Parameter(description = "ID of the transaction record to update", required = true)
            @PathVariable Long transactionRecordId,
            @Parameter(description = "Request body containing the updated transaction record", required = true)
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
