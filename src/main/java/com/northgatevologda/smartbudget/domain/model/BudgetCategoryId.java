package com.northgatevologda.smartbudget.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class BudgetCategoryId implements Serializable {
    private static final long serialVersionUID = 3136608523177752784L;
    @NotNull
    @Column(name = "budget_id", nullable = false)
    private Long budgetId;

    @NotNull
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BudgetCategoryId entity = (BudgetCategoryId) o;
        return Objects.equals(this.budgetId, entity.budgetId) &&
                Objects.equals(this.categoryId, entity.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(budgetId, categoryId);
    }

}
