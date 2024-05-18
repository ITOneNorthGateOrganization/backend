package com.northgatevologda.smartbudget.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transaction", schema = "public")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_type_id", nullable = false)
    private TransactionType transactionType;

    @CreationTimestamp
    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(name = "sender_id")
    private Long senderId;

    @Column(name = "sender_balance", nullable = false)
    private BigDecimal senderBalance;

    @Column(name = "receiver_balance", nullable = false)
    private BigDecimal receiverBalance;

    @OneToMany(mappedBy = "transaction", fetch = FetchType.LAZY)
    private List<TransactionRecord> transactionRecords = new ArrayList<>();

}