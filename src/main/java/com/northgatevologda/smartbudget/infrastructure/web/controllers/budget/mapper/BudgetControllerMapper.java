package com.northgatevologda.smartbudget.infrastructure.web.controllers.budget.mapper;

import com.northgatevologda.smartbudget.application.service.budget.dto.BudgetDTO;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.budget.dto.BudgetCreateRequest;
import com.northgatevologda.smartbudget.infrastructure.web.controllers.budget.dto.BudgetUpdateRequest;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for mapping BudgetCreateRequest to BudgetDTO.
 */
@Component
public class BudgetControllerMapper {

    /**
     * Maps BudgetUpdateRequest to BudgetDTO.
     *
     * @param budgetUpdateRequest The budget update request object.
     * @return BudgetDTO containing the id, importance, limit, periodic, and purpose from the budget update request.
     */
    public BudgetDTO toBudgetDTO(BudgetUpdateRequest budgetUpdateRequest) {
        return BudgetDTO.builder()
                .id(budgetUpdateRequest.getId())
                .importance(budgetUpdateRequest.getImportance())
                .limit(budgetUpdateRequest.getLimit())
                .cron(budgetUpdateRequest.getCron())
                .purpose(budgetUpdateRequest.getPurpose())
                .build();
    }

    /**
     * Maps BudgetCreateRequest to BudgetDTO.
     *
     * @param budgetCreateRequest The budget save request object.
     * @return BudgetDTO containing the limit, importance, purpose, and periodic from the budget save request.
     */
    public BudgetDTO toBudgetDTO(BudgetCreateRequest budgetCreateRequest) {
        return BudgetDTO.builder()
                .limit(budgetCreateRequest.getLimit())
                .importance(budgetCreateRequest.getImportance())
                .purpose(budgetCreateRequest.getPurpose())
                .cron(budgetCreateRequest.getCron())
                .build();
    }
}
