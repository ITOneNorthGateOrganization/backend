package com.northgatevologda.smartbudget.infrastructure.web.controllers.account.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountCreateRequest {
    @DecimalMin(value = "0")
    @NotNull
    private BigDecimal balance;
    @NotNull
    private String name;
}
