package com.northgatevologda.smartbudget.infrastructure.jpa.reports;

import com.northgatevologda.smartbudget.domain.model.Category;
import com.northgatevologda.smartbudget.domain.model.CategoryReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface JpaCategoryReportRepository extends JpaRepository<Category, Long> {
    @Query(
        nativeQuery = true,
        value = "SELECT id, category_id as categoryId, category_name as categoryName, period_start as periodStart, amount FROM get_categories_report(:username, :start_date, :end_date, :step)"
    )
    List<CategoryReportProjection> getReport(
        @Param("username") String username,
        @Param("start_date") String startDate,
        @Param("end_date") String endDate,
        @Param("step") BigDecimal step
    );
}
