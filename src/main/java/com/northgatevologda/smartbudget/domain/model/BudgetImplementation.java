package com.northgatevologda.smartbudget.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "budget_implementation", schema = "public")
public class BudgetImplementation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id")
    private Budget budget;

    @Column(name = "period_start")
    private Instant periodStart;

    @Column(name = "planned_period_end")
    private Instant plannedPeriodEnd;

    @Column(name = "period_end")
    private Instant periodEnd;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "\"limit\"")
    private BigDecimal limit;

}