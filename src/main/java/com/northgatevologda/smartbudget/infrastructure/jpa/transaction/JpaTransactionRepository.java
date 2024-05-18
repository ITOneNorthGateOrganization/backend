package com.northgatevologda.smartbudget.infrastructure.jpa.transaction;

import com.northgatevologda.smartbudget.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface JpaTransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(nativeQuery = true, value = """
                SELECT
                    t.*
                FROM
                    transaction t
                    INNER JOIN account a ON (
                        t.account_id = a.id
                    )
                WHERE
                    a.id = :accountId
                    AND a.user_id = :userId
            """)
    List<Transaction> findTransactionsByUserIdAndAccountId(Long userId, Long accountId);

    @Query(nativeQuery = true, value = """
                SELECT
                    t.*
                FROM
                    transaction t
                    INNER JOIN account a ON (
                        t.account_id = a.id
                    )
                WHERE
                    a.user_id = :userId
            """)
    List<Transaction> findTransactionsByUserId(Long userId);

    @Query(nativeQuery = true, value = """
                SELECT
                    t1.*
                FROM
                    transaction t1
                    INNER JOIN account a ON (
                        t1.account_id = a.id
                    )
                WHERE
                    a.user_id = :userId
                    AND t1.transaction_type_id = 2 -- Расход
                    AND EXISTS(
                        SELECT
                            NULL
                        FROM
                            (
                                SELECT
                                    SUM(tr.amount) AS amount
                                FROM
                                    transaction_record tr
                                WHERE
                                    tr.transaction_id = t1.id
                            ) t2
                        WHERE
                            t1.amount = t2.amount
                    )
            """)
    List<Transaction> findUnaccountedTransactionsByUserId(Long userId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
                UPDATE
                    transaction
                SET
                    description = :description
                WHERE
                    id = :transactionId
            """)
    void changeDescription(Long transactionId, String description);

    @Query(nativeQuery = true, value = """
                SELECT
                    DISTINCT t.*
                FROM
                    budget b
                    INNER JOIN transaction_record tr ON (
                        b.id = tr.budget_id
                    )
                    INNER JOIN transaction t ON (
                        tr.transaction_id = t.id
                    )
                WHERE
                    b.id = :budgetId
                    AND b.user_id = :userId
            """)
    List<Transaction> findTransactionsByUserIdAndBudgetId(Long userId, Long budgetId);
}
