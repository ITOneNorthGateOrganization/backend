package com.northgatevologda.smartbudget.infrastructure.jpa.budgetimplementation;

import com.northgatevologda.smartbudget.domain.model.BudgetImplementation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetImplementationRepository extends JpaRepository<BudgetImplementation, Long> {
    @Query(nativeQuery = true, value = """
                SELECT
                    bi.*
                FROM
                    budget_implementation bi
                WHERE
                    bi.budget_id = :budgetId
                    AND bi.period_end IS NULL
                LIMIT 1
            """)
    Optional<BudgetImplementation> findUnfinishedByBudgetId(Long budgetId);
}
