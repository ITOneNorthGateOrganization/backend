package com.northgatevologda.smartbudget.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ETransactionType {
    REPLENISHMENT(1),
    EXPENDITURE(2),
    TRANSLATION(3);

    private final int id;
}
