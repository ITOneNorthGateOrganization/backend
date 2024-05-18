package com.northgatevologda.smartbudget.infrastructure.jpa.reports;

import com.northgatevologda.smartbudget.domain.model.Account;
import com.northgatevologda.smartbudget.domain.model.BankrollChangesReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface JpaBankrollChangesReportRepository extends JpaRepository<Account, Long> {
    @Query(
        nativeQuery = true,
        value = "SELECT transaction_type_id AS transactionTypeId, period_start as periodStart, amount FROM get_bakroll_changes_report(:username, :start_date, :end_date, :step)"
    )
    List<BankrollChangesReportProjection> getReport(
        @Param("username") String username,
        @Param("start_date") String startDate,
        @Param("end_date") String endDate,
        @Param("step") BigDecimal step
    );
}
