package com.northgatevologda.smartbudget.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "bank", schema = "public")
public class Bank {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "bic", nullable = false)
    private Long bic;

    @UpdateTimestamp
    @Column(name = "update_at", nullable = false)
    private Instant updateAt;

    @NotNull
    @Column(name = "api_url", nullable = false, length = Integer.MAX_VALUE)
    private String apiUrl;

    @NotNull
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

}
