package com.northgatevologda.smartbudget.domain.ports.out;

import com.northgatevologda.smartbudget.domain.model.BudgetImplementationProjection;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * This interface represents a port for generating budget implementation percentage report.
 */
public interface BudgetProportionsReportRepositoryPort {
     /**
      * Return budget implementation percentage report
      * @param username Username
      * @param startDate start of period
      * @param endDate end od Period
      * @return list of BudgetImplementationProjection
      */
     List<BudgetImplementationProjection> getReport(String username, Instant startDate, Instant endDate);

     /**
      * Return sum of user's budget implementations for a period
      * @param username Username
      * @param startDate start of period
      * @param endDate end od Period
      * @return sum of user's budget implementations
      */
     BigDecimal getSumImplementation(String username, Instant startDate, Instant endDate);
}
