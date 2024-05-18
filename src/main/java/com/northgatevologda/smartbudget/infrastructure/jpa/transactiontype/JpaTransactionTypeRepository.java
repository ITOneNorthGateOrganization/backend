package com.northgatevologda.smartbudget.infrastructure.jpa.transactiontype;

import com.northgatevologda.smartbudget.domain.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTransactionTypeRepository extends JpaRepository<TransactionType, Long> {
}
