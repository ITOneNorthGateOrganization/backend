package com.northgatevologda.smartbudget.infrastructure.jpa.reports;

import com.northgatevologda.smartbudget.domain.model.Budget;
import com.northgatevologda.smartbudget.domain.model.BudgetImplementationProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Repository
public interface JpaBudgetProportionReportRepository extends JpaRepository<Budget, Long> {
    @Query(
        nativeQuery = true,
        value= """
                SELECT b.id, b.purpose, SUM(t.amount) AS amount
                  FROM budget b
                  JOIN transaction_record tr ON tr.budget_id = b.id
                  JOIN transaction t ON t.id = tr.transaction_id
                  JOIN public."user" u on b.user_id = u.id
                 WHERE t.created_at BETWEEN :start_date AND :end_date
                   AND u.username = :username
                 GROUP BY b.id, b.purpose
                """
    )
    List<BudgetImplementationProjection> getReport(
        @Param("username") String username,
        @Param("start_date") Instant startDate,
        @Param("end_date") Instant endDate
    );

    @Query(
        nativeQuery = true,
        value = """
                SELECT SUM(t.amount) AS amount
                  FROM budget b
                  JOIN transaction_record tr ON tr.budget_id = b.id
                  JOIN transaction t ON t.id = tr.transaction_id
                  JOIN public."user" u on b.user_id = u.id
                 WHERE t.created_at BETWEEN :start_date AND :end_date
                   AND u.username = :username
                """
    )
    BigDecimal getSumImplementation(
        @Param("username") String username,
        @Param("start_date") Instant startDate,
        @Param("end_date") Instant endDate
    );
}
