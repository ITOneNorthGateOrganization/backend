package com.northgatevologda.smartbudget.application.service.account.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link com.northgatevologda.smartbudget.domain.model.Account}
 */
@Data
@Builder
public class AccountDTO {
    private Long id;
    private BigDecimal balance;
    private boolean open;
    private Instant updateAt;
    private String name;
}