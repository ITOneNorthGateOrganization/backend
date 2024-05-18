package com.northgatevologda.smartbudget.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public interface BankrollChangesReportProjection {
    Integer getTransactionTypeId();

    Instant getPeriodStart();

    BigDecimal getAmount();

    Integer INCOME_TYPE_ID = 1;
    Integer EXPENSES_TYPE_ID = 2;
    Integer BALANCE_TYPE_ID = 0;
}
