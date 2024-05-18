package com.northgatevologda.smartbudget.infrastructure.jpa.budget;

import com.northgatevologda.smartbudget.domain.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface JpaBudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findBudgetsByUserId(Long userId);
    List<Budget> findBudgetsByClose(boolean close);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
        DELETE FROM budget_category WHERE budget_id = :id;
        DELETE FROM budget WHERE id = :id;
    """)
    void deleteBudgetById(Long id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "INSERT INTO budget_category (budget_id, category_id) VALUES (?, ?)")
    void tieUpCategory(Long budgetId, Long categoryId);

    @Query(nativeQuery = true, value = """
                SELECT
                    EXISTS(
                        SELECT
                            NULL
                        FROM
                            budget_category bc
                        WHERE
                            bc.budget_id = :budgetId
                            AND bc.category_id = :categoryId
                    )
            """)
    boolean checkTieUppedCategory(Long budgetId, Long categoryId);
}
