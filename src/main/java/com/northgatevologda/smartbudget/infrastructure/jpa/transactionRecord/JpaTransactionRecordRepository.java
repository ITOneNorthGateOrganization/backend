package com.northgatevologda.smartbudget.infrastructure.jpa.transactionrecord;

import com.northgatevologda.smartbudget.domain.model.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface JpaTransactionRecordRepository extends JpaRepository<TransactionRecord, Long> {
    List<TransactionRecord> getTransactionRecordsByTransactionId(Long transactionId);

    @Query(nativeQuery = true, value = """
                SELECT
                    *
                FROM
                    transaction_record tr
                    INNER JOIN transaction t ON (
                        tr.transaction_id = t.id
                    )
                WHERE
                    tr.budget_id = :budgetId
                    AND t.created_at BETWEEN :startDate AND :endDate
            """)
    List<TransactionRecord> findTransactionRecordsByBudgetIdAndPeriod(
            Long budgetId,
            Instant startDate,
            Instant endDate
    );
}
