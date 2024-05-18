package com.northgatevologda.smartbudget.domain.model;

import java.math.BigDecimal;

public interface BudgetImplementationProjection {
    Long getId();
    String getPurpose();
    BigDecimal getAmount();
}
