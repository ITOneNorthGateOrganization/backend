package com.northgatevologda.smartbudget.infrastructure.jpa.category;

import com.northgatevologda.smartbudget.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface JpaCategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoryByName(String name);

    List<Category> findCategoriesByForEveryone(boolean forEveryone);

    @Query(nativeQuery = true, value = """
                SELECT
                    EXISTS(
                        SELECT
                            NULL
                        FROM
                            user_category uc
                        WHERE
                            uc.user_id = :userId
                            AND uc.category_id = :id
                    )
            """)
    boolean existsCategoryByIdForUserId(Long id, Long userId);

    @Query(nativeQuery = true, value = """
                SELECT
                    c.*
                FROM
                    category c
                    INNER JOIN user_category uc ON (
                        c.id = uc.category_id
                    )
                    INNER JOIN "user" u ON (
                        uc.user_id = u.id
                    )
                WHERE
                    u.id = :userId
            """)
    List<Category> findCategoriesByUserId(Long userId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO user_category (user_id, category_id) VALUES (:userId, :categoryId)", nativeQuery = true)
    void tieUpCategoryToUser(Long categoryId, Long userId);
}
