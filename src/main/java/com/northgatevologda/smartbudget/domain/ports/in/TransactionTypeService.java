package com.northgatevologda.smartbudget.domain.ports.in;

import com.northgatevologda.smartbudget.domain.model.TransactionType;

/**
 * Service interface for managing transaction types.
 */
public interface TransactionTypeService {

    /**
     * Find a transaction type by its ID.
     *
     * @param id The ID of the transaction type to find.
     * @return The transaction type found.
     */
    TransactionType findById(Long id);
}