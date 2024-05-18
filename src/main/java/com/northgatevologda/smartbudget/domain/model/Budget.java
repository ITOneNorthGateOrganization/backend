package com.northgatevologda.smartbudget.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "budget", schema = "public")
public class Budget {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "\"limit\"", nullable = false)
    private BigDecimal limit;

    @NotNull
    @Column(name = "importance", nullable = false)
    private Long importance;

    @NotNull
    @Column(name = "purpose", nullable = false, length = Integer.MAX_VALUE)
    private String purpose;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "cron", nullable = false, length = 100)
    private String cron;

    @Column(name = "close", nullable = false)
    private boolean close;

    @OneToMany(mappedBy = "budget", fetch = FetchType.LAZY)
    private List<TransactionRecord> transactionRecords = new ArrayList<>();

}
