package com.northgatevologda.smartbudget.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public interface CategoryReportProjection {
    Long getId();

    Long getCategoryId();

    String getCategoryName();

    Instant getPeriodStart();

    BigDecimal getAmount();
}